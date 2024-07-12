package com.hm.action.guild.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.hm.action.item.ItemBiz;
import com.hm.libcore.language.LanguageVo;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.tips.TipsBiz;
import com.hm.annotation.Broadcast;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.GuildJob;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.TipsType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.Log;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.guild.bean.GuildPlayerVo;
import com.hm.model.guild.bean.GuildReqPlayer;
import com.hm.model.item.Items;
import com.hm.model.player.BaseTips;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.guild.GuildContainer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * ClassName: GuildMemberBiz. <br/>  
 * date: 2018年11月16日 下午2:50:45 <br/>  
 * 部落成员管理
 * @author zxj  
 * @version
 */
@Slf4j
@Biz
public class GuildMemberBiz extends NormalBroadcastAdapter {

	@Resource
	private GuildBiz guildBiz;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private GuildWorldBiz guildWorldBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private GuildBarrackBiz guildBarrackBiz;
	@Resource
	private GuildFactoryBiz guildFactoryBiz;
	@Resource
	private TipsBiz tipsBiz;
	@Resource
	private GuildConfig guildConfig;

	//更改部落官员职位
	public boolean changeJob(Player tarPlayer, int officId, Guild guild) {
		guild.getGuildMembers().changeJob(tarPlayer.getId(), GuildJob.getType(officId));
		log.error("修改玩家职务信息：" + tarPlayer.getId() + ";" + officId + ";" + guild.getId());
		tarPlayer.playerGuild().SetChanged();
		tarPlayer.notifyObservers(ObservableEnum.GuildChangeJob);
		tarPlayer.sendUserUpdateMsg();
		return true;
	}
	//自动清除部落长时间不在线的 用户
	public void autoClearMember(Guild guild) {
		List<GuildPlayer> listMember = guild.getGuildMembers().getGuildMembers();
		if(CollectionUtil.isEmpty(listMember)) {
			Log.Info(String.format("删除没有成员的部落：%s", guild.getId()));
			guildBiz.delGuild(guild);
			return;
		}
		for(GuildPlayer guildPlayer:listMember) {
			PlayerRedisData redisPlayer = RedisUtil.getPlayerRedisData(guildPlayer.getPlayerId());
			Player tarPlayer = null;
			if(null==redisPlayer) {
				tarPlayer = PlayerUtils.getPlayer(guildPlayer.getPlayerId());
				if(null==tarPlayer) {
					Log.Info(String.format("删除不存在用户：%s", guildPlayer.getPlayerId()));
					delGuildSave(guildPlayer.getPlayerId(), guild);
					continue;
				}else {
					redisPlayer = new PlayerRedisData(tarPlayer);
					RedisUtil.updateRedisPlayer(tarPlayer);
				}
			}
		}
		
	}

	//申请加入部落
	public void joinGild(Player player, Guild guild) {
		int hour = commValueConfig.getCommValue(CommonValueType.GuildApplyTime);
		long endTime = System.currentTimeMillis() + hour * GameConstants.HOUR;
		player.playerGuild().addReq(guild.getId());
		guild.addReqPlayer(player, endTime);
		guild.saveDB();
		guildBiz.sendPlayerGuildVo(player);
	}

	//取消部落申请
	public void cancel(Player player, Guild guild) {
		player.playerGuild().removeReq(guild.getId());
		guild.removeReq(player);
		guild.saveDB();
		guildBiz.sendPlayerGuildVo(player);
	}
	//一键拒绝申请
	public boolean refuse(Guild guild) {
		List<Player> reqPlayerList = guild.getGuildReqs().getPlayerList();
		return refuseReqPlayers(guild, reqPlayerList);
	}

	// 检查申请是否已过期 过期的自动拒绝
	public boolean checkAndRefuseEndTime(Guild guild) {
		List<Player> reqPlayerList = guild.getGuildReqs().getGuildMembers().stream()
				.filter(GuildReqPlayer::isEnd)
				.map(e -> PlayerUtils.getPlayer(e.getPlayerId()))
				.collect(Collectors.toList());
		return refuseReqPlayers(guild, reqPlayerList);
	}

	private boolean refuseReqPlayers(Guild guild, List<Player> reqPlayerList) {
		//给所有拒绝者发邮件
		Set<Long> receivers = reqPlayerList.stream().map(e -> e.getId()).collect(Collectors.toSet());
		mailBiz.sendSysMail(guild.getServerId(), receivers, MailConfigEnum.GuildRefuse, null, LanguageVo.createStr(guild.getGuildInfo().getGuildName()));
		//清除玩家申请部落信息
		reqPlayerList.forEach(player -> {
			player.playerGuild().removeReq(guild.getId());
			player.sendUserUpdateMsg();
			guild.getGuildReqs().removePlayer(player.getId());
		});
		return true;
	}

	public void refusePlayerReq(Guild guild, Player reqPlayer) {
		//给拒绝者发邮件
		mailBiz.sendSysMail(reqPlayer, MailConfigEnum.GuildRefuse, null,LanguageVo.createStr(guild.getGuildInfo().getGuildName()));

		reqPlayer.playerGuild().removeReq(guild.getId());
		reqPlayer.sendUserUpdateMsg();
		guild.getGuildReqs().removePlayer(reqPlayer.getId());
	}

	//部落领导，同意加入部落
	public void agree(Player player, Player reqBasePlayer, Guild guild) {
		this.playerAddGuild(guild, reqBasePlayer, GuildJob.NONE);

		reqBasePlayer.sendUserUpdateMsg();
		guild.sendPlayerGuild(player);
		guild.sendPlayerAllGuild(reqBasePlayer);
		//发送同意邮件
		mailBiz.sendSysMail(reqBasePlayer, MailConfigEnum.GuildAgree, null,LanguageVo.createStr(guild.getGuildInfo().getGuildName()));
	}

	//玩家同意加入部落邀请
	public void playerAddGuild(Guild guild, Player player, GuildJob job) {
		guild.getGuildMembers().addPlayer(new GuildPlayer(player, job));
		guild.getGuildInvite().removeInvite(player.getId());
		checkAndSendJoinReward(player);
		player.playerGuild().setGuild(guild);
		// 玩家申请列表
		List<Integer> reqList = player.playerGuild().getReqGuild();
		reqList.forEach(a->{
			Guild reqGuild = GuildContainer.of(player.getServerId()).getGuild(a);
			if(null!=reqGuild) {
				reqGuild.getGuildReqs().removePlayer(player.getId());
				reqGuild.saveDB();
			}
		});
		player.playerGuild().clearReq();
		guildFactoryBiz.checkPlayerArmsOpen(player, guild);
		player.notifyObservers(ObservableEnum.GuildPlayerAdd, guild);
		guild.saveDB();
	}

	private void checkAndSendJoinReward(Player player) {
		// 首次加入奖励
		if (player.playerGuild().canFirstAddReward()){
			List<Items> rewards = commValueConfig.getListItem(CommonValueType.GuildFirstAddReward);
			mailBiz.sendSysMail(player, MailConfigEnum.FirstJoinGuild, rewards);
		}
	}

	public void quit(Player player){
		Guild guild = guildBiz.getGuild(player);
		log.error(guild.getId()+"成员退出"+player.getId());
		// 最后一个成员  部落解散
		if (guild.getGuildMembers().isLastMember()) {
			delGuild(player, guild);
			return;
		}
		if(guild.isLeader(player)) {
			long toPlayerId = guild.getGuildMembers().randomNextPresident();
			log.error("玩家{} 退出军团{}后 {}接任", player.getId(), guild.getId(), toPlayerId);
			Player toLeader = PlayerUtils.getPlayer(toPlayerId);
			guildBiz.transfer(player, guild, toLeader);
		}
		quitGuildSave(player, guild);
	}

	//首领退出并删除部落
	public void delGuild(Player player, Guild guild) {
		//删除领地信息
		guildWorldBiz.clearGuildArea(player,guild);

		removeGuildMembers(player, guild);
		this.quitGuild(player, guild);

		RedisTypeEnum.Guild.del(guild.getId());
		guildBiz.delGuild(guild);
		log.error("部落删除:"+guild.getId() +","+guild.getGuildInfo().getGuildName());
	}

	// 移除成员
	private void removeGuildMembers(Player player, Guild guild) {
		BaseTips delTips = TipsType.KickGuild.createTips(player.getName(), guild.getGuildInfo().getGuildName());
		guild.getGuildMembers().getPlayerList().forEach(member ->{
			if (!member.getId().equals(player.getId())){
				this.quitGuild(member, guild);
				tipsBiz.sendPlayerTips(member, delTips);
				player.sendUserUpdateMsg();
				player.sendMsg(MessageComm.S2C_Guild_Quit, true);
			}
		});
	}

	//用户退出并保存部落
	public void quitGuildSave(Player player, Guild guild) {
		this.quitGuild(player, guild);
		GuildPlayer guildPlayer = guild.getGuildMembers().getPresidentGuildPlayer();
		if(guildPlayer == null) {
			return;
		}
		guildFactoryBiz.armsStrengthDown(guild,player);
		guildBiz.sendFailMail(guild, PlayerUtils.getPlayer(guildPlayer.getPlayerId()), player);
		guild.removePlayer(player.getId());
		guild.saveDB();
		guild.broadMemberGuildUpdate();
		player.sendUserUpdateMsg();
	}
	
	//删除不存在的用户，保存部落
	public void delGuildSave(long playerId, Guild guild) {
		guild.removePlayer(playerId);
		guild.saveDB();
	}
	
	//用户退出部落信息
	private void quitGuild(Player player, Guild guild) {
		//离开部落大营
		player.playerGuild().quit();
		guild.sendPlayerGuild(player);
		player.notifyObservers(ObservableEnum.GuildPlayerQuit, guild);
		broadGuildPlayerQuit(player, guild);
		Log.Info(String.format("%s-玩家退出部落：%s", player.getId(), guild.getGuildInfo().getGuildName()));
	}

	// 部落成员数量是否达到最大值
	public boolean isGuildMemberMax(Guild guild){
		int maxNum = guildConfig.getGuildMemberNum(guild.guildLevelInfo().getLv());
		return guild.getGuildMembers().getNum() >= maxNum;
	}

	// 副首领数量是否数量已达最大值
	public boolean isGuildLegatesMax(Guild guild){
		long nowNum = guild.getGuildMembers().getJobSize(GuildJob.LEGATUS);
		int maxNum = commValueConfig.getCommValue(CommonValueType.legatusSize);
		return nowNum >= maxNum;
	}

	@Broadcast({ObservableEnum.LOGIN, ObservableEnum.GuildPlayerAdd})
	public void noticeGuildPlayerIn(ObservableEnum observableEnum, Player player, Object... argv){
		if (!player.isOnline()){
			return;
		}
		Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
		if (guild != null){
			GuildPlayer guildPlayer = guild.getGuildMembers().guildPlayer(player.getId());
			guildPlayer.loadPlayerInfo(player);

			if (guildPlayer!= null){
				JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Broad_MemberIn);
				serverMsg.addProperty("guildPlayer", GuildPlayerVo.buildVo(guildPlayer));
				guildBiz.broadGuildMember(guild, serverMsg);
			}
		}
	}

	@Broadcast(ObservableEnum.PlayerLoginOut)
	public void noticeGuildPlayerOut(ObservableEnum observableEnum, Player player, Object... argv){
		Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
		if (guild != null){
			GuildPlayer guildPlayer = guild.getGuildMembers().guildPlayer(player.getId());
			if (guildPlayer!= null){
				broadGuildPlayerOut(player, guild);
			}
		}
	}

	// 部落广播玩家离线
	private void broadGuildPlayerOut(Player player, Guild guild) {
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Broad_MemberLogout);
		serverMsg.addProperty("playerId", player.getId());
		guildBiz.broadGuildMember(guild, serverMsg);
	}

	// 部落广播玩家退出
	private void broadGuildPlayerQuit(Player player, Guild guild) {
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Broad_MemberQuit);
		serverMsg.addProperty("playerId", player.getId());
		guildBiz.broadGuildMember(guild, serverMsg);
	}



	@Broadcast(ObservableEnum.HourEvent)
	public void doHourEvent(ObservableEnum observableEnum, Player player, Object... argv){
		if(DateUtil.thisHour(true) == 4) {
			checkGuildMember();
		}
	}

	@Broadcast(ObservableEnum.ChangeName)
	public void doChangeName(ObservableEnum observableEnum, Player player, Object... argv){
		Guild guild = guildBiz.getGuild(player);
		if (guild != null && guild.isLeader(player)){
			guild.getGuildInfo().setLeaderName(player.getName());
			guild.broadMemberGuildUpdate();
			guild.saveDB();
		}
	}

	/**
	 * 凌晨4点检查
	 */
	public void checkGuildMember() {
		//检查部落成员
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			try {
				GuildContainer.of(serverId).getAllGuild().forEach(guild -> {
					autoClearMember(guild);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}




