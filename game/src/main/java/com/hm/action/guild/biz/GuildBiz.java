package com.hm.action.guild.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.action.guild.vo.GuildVo;
import com.hm.libcore.language.LanguageVo;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.container.PlayerContainer;
import com.hm.db.GuildDbUtils;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.GuildImpeachStateEnum;
import com.hm.enums.GuildJob;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildInviteBean;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.guild.bean.GuildPlayerVo;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.observer.IObservable;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.redis.util.RedisUtil;
import com.hm.servercontainer.guild.GuildContainer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * ClassName: GuildBiz. <br/>  
 * Function:部落信息处理. <br/>
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年11月16日 上午11:42:47 <br/>  
 *  
 * @author zxj  
 * @version
 */
@Slf4j
@Biz
public class GuildBiz implements IObservable{

	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private GuildMemberBiz guildMemberBiz;
	@Resource
	private GuildFlagBiz guildFlagBiz;
	@Resource
	private GuildCityBiz guildCityBiz;
	

	/**
	 * create:(玩家申请创建部落). <br/>
	 * @author zxj  
	 * @param player
	 * @param name
	 * @return  使用说明
	 */
	public Guild create(Player player, String name) {
		Guild guild = new Guild(player.getServerId());
		guild.init(player, name, guildFlagBiz.randomGuildFlag(player.getServerId()));
		log.error("用户创建部落，并成为部落长："+ player.getId() + "," + guild.isLeader(player));
		GuildContainer.of(guild).addGuild(guild);
		player.notifyObservers(ObservableEnum.GuildCreate, guild);
		guildMemberBiz.playerAddGuild(guild, player, GuildJob.PRESIDENT);
		return guild;
	}

	//转让部落
	public boolean transfer(Player player, Guild guild, Player transPlayer) {
		guild.transfer(player, transPlayer);
		log.error("转让部落长职务："+ player.getId() + ";转让给："+transPlayer.getId());
		guild.saveDB();
		player.playerGuild().SetChanged();
		transPlayer.playerGuild().SetChanged();
		transPlayer.sendUserUpdateMsg();
		player.sendUserUpdateMsg();
		this.sendPlayerGuildVo(player);
		this.sendGuildMember(player);
		this.sendPlayerGuildVo(transPlayer);
		this.sendGuildMember(transPlayer);
		return true;
	}
	//修改公告信息
	public void changeNotice(Guild guild, Player player, String notice) {
		guild.getGuildInfo().changeNotice(notice);
		guild.saveDB();
	}

	//获取部分活跃部落
	public List<GuildVo> getActiveGuildRankList(int serverId, List<Integer> excludeIds, AtomicBoolean isEnough) {
		// 最大活跃数量
		int activeTotal = commValueConfig.getCommValue(CommonValueType.GuildActiveTotal);
		// 获取活跃部落
		List<GuildVo> activeGuildVos = getActiveGuildList(serverId, activeTotal);
		// 获得并排除部分已展示的部落
		List<GuildVo> excludeGuilds = getAndDoExcludeGuildVos(excludeIds, activeGuildVos);
		// 单次展示数量
		int onceNum = commValueConfig.getCommValue(CommonValueType.GuildOnceNum);
		// 剩余数量是否还够刷新一次的
		isEnough.set(activeGuildVos.size() <= onceNum);
		Collections.shuffle(activeGuildVos);
		// 将展示过的部落放到末尾
		activeGuildVos.addAll(excludeGuilds);

		return activeGuildVos.stream().limit(onceNum).collect(Collectors.toList());
	}

	private List<GuildVo> getAndDoExcludeGuildVos(List<Integer> excludeIds, List<GuildVo> activeGuildVos) {
		List<GuildVo> excludeGuilds = Lists.newArrayList();
		Iterator<GuildVo> it = activeGuildVos.iterator();
		while (it.hasNext()){
			GuildVo vo = it.next();
			if (excludeIds.contains(vo.getGuildId())){
				excludeGuilds.add(vo);
				it.remove();
			}
		}
		return excludeGuilds;
	}

	private List<GuildVo> getActiveGuildList(int serverId, int activeTotal) {
		int activeDay = commValueConfig.getCommValue(CommonValueType.GuildActiveDay);
		// 活跃时间
		long activeLastTime = System.currentTimeMillis() - activeDay * GameConstants.DAY;
		// 所有的部落
		List<Guild> allGuild = GuildContainer.of(serverId).getAllGuild();
		if (allGuild.size() <= activeTotal){// 少于15个全部返回
			return GuildContainer.of(serverId).getAllGuild().stream()
					.map(e -> GuildVo.buildVo(e))
					.collect(Collectors.toList());
		} else {
			// 活跃部落数量
			List<GuildVo> activeGuildVos = getActiveGuildVos(allGuild, activeLastTime);
			// 数量不足15个 补足15
			if (activeGuildVos.size() < activeTotal){
				List<GuildVo> lastVos = allGuild.stream().filter(e -> e.getGuildInfo().getLastLeaderLoginTime() < activeLastTime)
						.map(e -> GuildVo.buildVo(e))
						.sorted(Comparator.comparingLong(GuildVo::getCombat).reversed())
						.limit(activeTotal - activeGuildVos.size()).collect(Collectors.toList());
				activeGuildVos.addAll(lastVos);
			}
			return activeGuildVos;
		}
	}

	private List<GuildVo> getActiveGuildVos(List<Guild> allGuild, long activeLastTime) {
		return allGuild.stream()
				.filter(e -> e.getGuildInfo().getLastLeaderLoginTime() >= activeLastTime)
				.map(e -> GuildVo.buildVo(e))
				.sorted(Comparator.comparingLong(GuildVo::getCombat).reversed())
				.collect(Collectors.toList());
	}

	/**
	 * sendGuildMember:(获取部落成员列表). <br/>
	 * @author zxj  
	 * @param player  使用说明
	 */
	public void sendGuildMember(Player player) {
		Guild guild = this.getGuild(player);
		List<GuildPlayerVo> guildPVo = guild.getGuildMembers().getGuildMembers().stream()
				.map(e -> GuildPlayerVo.buildVo(e)).collect(Collectors.toList());
		player.sendMsg(MessageComm.S2C_Guild_PlayerChange, guildPVo);
	}

	/**
	 * sendReqMember:(获取申请的成员列表). <br/>  
	 * @author zxj
	 * @param guild  使用说明
	 */
	public void sendReqMember(Player player, Guild guild) {
		List<GuildPlayerVo> guildReqPVo = guild.getGuildReqs().getGuildMembers().stream()
				.map(e -> GuildPlayerVo.buildVo(e)).collect(Collectors.toList());
		player.sendMsg(MessageComm.S2C_Guild_GetReq, guildReqPVo);
	}
	
	//获取用户所在的部落
	public Guild getGuild(BasePlayer player) {
		int guildId = player.playerGuild().getGuildId();
		if (guildId <= 0){
			return null;
		}
		return GuildContainer.of(player).getGuild(player.playerGuild().getGuildId());
	}

	public void sendPlayerGuildVo(Player player) {
		Guild guild = this.getGuild(player);
		if(guild != null) {
			guild.sendPlayerAllGuild(player);
		}else{
			if(player.playerGuild().getGuildId() > 0) {
				log.error("玩家{}的部落{}找不到", player.getId(), player.playerGuild().getGuildId());
				player.playerGuild().quit();
			}
		}
	}
	
	//用户增加部落贡献
	public void addContr(BasePlayer player, long num) {
		Guild guild = this.getGuild(player);
		if(guild == null) {
			return;
		}
		guild.getGuildMembers().addContr(player, num);
		guild.saveDB();
	}
	
	//删除部落信息
	public void delGuild(Guild guild) {
		GuildDbUtils.delete(guild);
		GuildContainer.of(guild.getServerId()).delGuild(guild.getId());
		RedisTypeEnum.Guild.del(guild.getId());
		this.notifyChanges(ObservableEnum.GuildDel, null, guild);
	}
	
	@Override
	public void notifyChanges(ObservableEnum observableEnum, Player player, Object... argv) {
		ObserverRouter.getInstance().notifyObservers(observableEnum, player, argv);
	}
	//清理以前邀请过的玩家。
	public void cleanInvite(Guild guild, Map<Long, GuildInviteBean> listPlayerId) {
        Iterator<Map.Entry<Long, GuildInviteBean>> it = listPlayerId.entrySet().iterator(); 
        boolean isChange = false;
        while(it.hasNext()){  
            Map.Entry<Long, GuildInviteBean> entry = it.next(); 
            Player tempPlayer = PlayerContainer.getPlayer(entry.getKey());
            //判断玩家在线不在线，或者已经在部落里面，或者超过一定时间
            int minute =commValueConfig.getCommValue(CommonValueType.GuildClearInvite);
			if(null==tempPlayer || tempPlayer.getGuildId()>0 
				|| DateUtil.between(entry.getValue().getInviteDate(), new Date(), DateUnit.MINUTE)>=minute) {
				it.remove();
				isChange = true;
			}
        }
        if(isChange) {
        	guild.getGuildInvite().SetChanged();
        	guild.saveDB();
        }
	}

	//广播消息->部落管理者
	public void broadGuildManager(Guild guild,JsonMsg msg) {
		guild.getGuildMembers().getGuildMembers().stream()
			.filter(t-> GuildJob.isManager(t.getGuildJob()))
			.map(e -> PlayerContainer.getPlayer(e.getPlayerId()))
			.filter(Objects::nonNull)
			.forEach(player -> player.sendMsg(msg));
	}

	//广播消息->部落所有成员
	public void broadGuildMember(Guild guild, JsonMsg msg) {
		if(guild == null) {
			return;
		}
		guild.getGuildMembers().getGuildMembers().stream()
			.map(e -> PlayerContainer.getPlayer(e.getPlayerId()))
			.filter(Objects::nonNull)
			.forEach(player -> player.sendMsg(msg));
	}
	
	public void updateGuildCombat(Guild guild) {
		List<PlayerRedisData> playerList = guild.getGuildMembers().getRedisPlayerList();
		for (PlayerRedisData player : playerList) {
			GuildPlayer guildPlayer = guild.getGuildMembers().guildPlayer(player.getId());
			if(guildPlayer != null) {
				guildPlayer.setCombat(player.getCombat());
			}
		}
	}
	
	public void updateLoginTime(Player p) {
		Guild guild = GuildContainer.of(p).getGuild(p.getGuildId());
		if(null==guild) {
			return;
		}
		guild.getGuildInfo().updateLoginTime();
		if (guild.isLeader(p)){
			guild.getGuildInfo().updateLeaderLoginTime();
		}
		guild.saveDB();
		RedisUtil.updateRedisGuild(guild);
	}
	/**
	 * checkGuildImpeach:(校验弹劾部落长的，当前状态，并发送结束邮件，是否已经结束). <br/>
	 * @author zxj  
	 * @param guild
	 * @return  使用说明
	 */
	public void checkGuildImpeach(Guild guild) {
		//现在没有进行部落长弹劾
		if(guild.getGuildImpeach().isEnd()) {
			return;
		}
		//弹劾正在进行中
		if(System.currentTimeMillis() < guild.getGuildImpeach().getEndDate()) {
			//还没有结束
			log.error("部落弹劾正在进行中："+guild.getId());
			return;
		}
		//发起人修改为部落团长
		long actMember = guild.getGuildImpeach().getActMember();
		Player player = PlayerUtils.getPlayer(actMember);
		GuildPlayer guildPlayer = guild.getGuildMembers().getPresidentGuildPlayer();
		Player tarPlayer = PlayerUtils.getPlayer(guildPlayer.getPlayerId());
		if(null == player || null == tarPlayer) {
			log.error(String.format("部落弹劾异常：%s;%s;%s", guild.getId(), null!=player, null!=tarPlayer));
			guild.getGuildImpeach().end();
			return;
		}

		//发送邮件的成员列表
		Set<Long> receivers = guild.getGuildMembers().getGuildMemberIds();
		if(CollectionUtil.isEmpty(receivers)) {
			log.error(String.format("部落弹劾异常：%s 没有部落成员", guild.getId()));
			guild.getGuildImpeach().end();
			return;
		}

		boolean suc = getImpeachResult(guild);
		if(suc) {
			//部落长修改为普通成员
			this.transfer(tarPlayer, guild, player);
		}
		guild.getGuildImpeach().end();

		MailConfigEnum mailConfigEnum = suc ? MailConfigEnum.GuildImpeachSucc : MailConfigEnum.GuildImpeachFail;
		LanguageVo languageVo = suc ? LanguageVo.createStr(player.getName()) : null;
		mailBiz.sendSysMail(guild.getServerId(), receivers, mailConfigEnum, null, languageVo);
	}

	// 获取投票结果
	private boolean getImpeachResult(Guild guild) {
		List<Integer> resultList = guild.getGuildImpeach().getReuslt();
		//成功(+1，表示加上发起者)
		int countAllow = Collections.frequency(resultList, GuildImpeachStateEnum.Allow.getType())+1;
		//失败的总数
		int refuse = Collections.frequency(resultList, GuildImpeachStateEnum.Refuse.getType());

		boolean suc = countAllow > refuse;
		log.error(guild.getId() +"部落弹劾:"+suc+",投票结果" + countAllow +":"+ refuse);
		return suc;
	}

	/**
	 * sendFailMail:(发送弹劾失败邮件。用于弹劾期间突然事件。提前结束弹劾). <br/>  
	 * @author zxj  
	 * @param guild
	 * @param leaderPlayer
	 * @param quitPlayer  使用说明
	 */
	public void sendFailMail(Guild guild, Player leaderPlayer, Player quitPlayer) {
		if(guild.getGuildImpeach().isEnd()) {
			return;
		}
		Player tarPlayer = null;
		if(null==quitPlayer) {
			tarPlayer = PlayerUtils.getPlayer(guild.getGuildImpeach().getActMember());
		}else if(quitPlayer.getId() == guild.getGuildImpeach().getActMember()){
			tarPlayer = quitPlayer;
		}
		if(null==tarPlayer) {
			return;
		}
		mailBiz.sendSysMail(guild, MailConfigEnum.GuildImpeachFail.getType(),null, LanguageVo.createStr(tarPlayer.getName(), leaderPlayer.getName(), leaderPlayer.getName()));
		guild.getGuildImpeach().end();
		guild.broadMemberGuildUpdate();
		guild.saveDB();
	}
	/**
	 * doHourJob:(部落每小时执行的检查). <br/>
	 * @author zxj  
	 * @param guild  使用说明
	 */
	public void doHourJob(Guild guild) {
		try {
			//更新部落战力
			if(DateUtil.thisHour(true) > 0) {
				this.updateGuildCombat(guild);
			}
			//检查部落的弹劾信息
			this.checkGuildImpeach(guild);
			// 检查申请是否已过期
			guildMemberBiz.checkAndRefuseEndTime(guild);

			if (guild.getGuildInfo().Changed() || guild.getGuildReqs().Changed() || guild.getGuildImpeach().Changed()){
				guild.broadMemberGuildUpdate();
				guild.saveDB();
			}
		} catch (Exception e) {
			log.error("部落每小时执行的检查异常：",e);
		}
	}
	/**
	 * 检查部落没有部落长的情况
	 * @param guild
	 */
	public void checkLeader(Guild guild) {
		try {
			GuildPlayer tempGuildPlayer = guild.getGuildMembers().getPresidentGuildPlayer();
			if(null==tempGuildPlayer) {
				log.error("部落中没有部落长："+guild.getId());
				long nextPlayerId = guild.getGuildMembers().randomNextPresident();
				log.error("下一任部落长："+nextPlayerId);
				if(nextPlayerId<=0) {
					log.error(String.format("修改部落长时，发送错误，未随机出下一个部落长：%s", guild.getId()));
					return;
				}
				Player nextPlayer = PlayerUtils.getPlayer(nextPlayerId);
				guildMemberBiz.changeJob(nextPlayer, GuildJob.PRESIDENT.getType(), guild);
				guild.getGuildInfo().setLeader(nextPlayer);
				//在线再发信息
				if(nextPlayer.isOnline()) {
					this.sendGuildMember(nextPlayer);
				}
				guild.broadMemberGuildUpdate();
				guild.saveDB();
				log.error("修改为下一任部落长："+nextPlayerId);
			}
		} catch (Exception e) {
			log.error("每小时处理部落长信息异常："+guild.getId(), e);
		}
	}
	
	//获取全服最大部落等级
	public int getGuildLvMax(int serverId){
		List<Guild> guilds = GuildContainer.of(serverId).getAllGuild();
		if(guilds.size()>0){
			return guilds.stream().mapToInt(t ->t.guildLevelInfo().getLv()).max().getAsInt();
		}
		return 0;
	}

	/**
	 * 部落相关数据
	 * @param player
	 * @param serverMsg
	 */
	public void fillGuildData(Player player, JsonMsg serverMsg){
		Guild guild = getGuild(player);
		serverMsg.addProperty("haveCityReward", guildCityBiz.haveGuildWorldCityReward(player, guild));
	}
}







