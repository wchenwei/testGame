package com.hm.action.guild;

import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildMemberBiz;
import com.hm.action.guild.util.GuildCheckUtil;
import com.hm.action.guild.vo.GuildVo;
import com.hm.action.tips.TipsBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildConfig;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.GuildJob;
import com.hm.enums.TipsType;
import com.hm.enums.UserSetTypeEnum;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildInviteBean;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Action
public class GuildMemberAction extends AbstractGuildAction{
	
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private GuildMemberBiz guildMemberBiz;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private GuildConfig guildConfig;
	@Resource
	private TipsBiz tipsBiz;
	
	//踢出成员 #msg:30017,playerId=100029
	@MsgMethod(MessageComm.C2S_Guild_Kick)
    public void kick(Player player, Guild guild, JsonMsg msg) {
		long playerId = msg.getInt("playerId");
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return;
		}
		Player transPlayer = PlayerUtils.getPlayer(playerId);
		if (guild.isLeader(transPlayer)){
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return;
		}
		log.error("{}踢出部落成员：{}",player.getId(), playerId);
		guildMemberBiz.quitGuildSave(transPlayer, guild);
		guildBiz.sendGuildMember(player);
		transPlayer.playerGuild().removeGuild();
		guildBiz.sendPlayerGuildVo(transPlayer);
		tipsBiz.sendPlayerTips(transPlayer, TipsType.KickGuild.createTips(player.getName(), guild.getGuildInfo().getGuildName()));
		transPlayer.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Guild_Kick, true);
	}

	//给成员换部落职位
	@MsgMethod(MessageComm.C2S_Guild_Job)
    public void changeOffic(Player player, Guild guild, JsonMsg msg) {
		long playerId = msg.getInt("playerId");
		int officId = msg.getInt("officId");
		if(!guild.isLeader(player) ||guild.getGuildInfo().getLeaderId()==playerId) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		//副团长数量限制
		if(GuildJob.LEGATUS.getType() ==officId && guildMemberBiz.isGuildLegatesMax(guild)) {
			player.sendErrorMsg(SysConstant.Guild_NumErr);
			return ;
		}
		//官职不能比自己高
		if(officId>=guild.getGuildMembers().getJob(player.getId()).getType()) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return;
		}
		//判断用户是否属于部落
		if(!guild.getGuildMembers().isGuildMember(playerId)) {
			player.sendErrorMsg(SysConstant.Guild_MemberQuit);
			return ;
		}
		//更换官职
		Player tarPlayer = PlayerUtils.getPlayer(playerId);
		boolean result = guildMemberBiz.changeJob(tarPlayer, officId, guild);
		guild.saveDB();
		guildBiz.sendGuildMember(tarPlayer);
		guildBiz.sendGuildMember(player);
		guildBiz.sendPlayerGuildVo(tarPlayer);
		player.sendMsg(MessageComm.S2C_Guild_Job, result);
	}

	//一键拒绝申请
	@MsgMethod(MessageComm.C2S_Guild_Refuse)
	public void refuse(Player player, Guild guild, JsonMsg msg) {
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return;
		}
		guildMemberBiz.refuse(guild);
		guildBiz.sendReqMember(player, guild);
		guild.saveDB();
		player.sendMsg(MessageComm.S2C_Guild_Refuse);
	}

	// 拒绝单个玩家申请
	@MsgMethod(MessageComm.C2S_Guild_RefusePlayer)
	public void refusePlayerReq(Player player, Guild guild, JsonMsg msg) {
		long playerId = msg.getInt("playerId");
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return;
		}
		Player reqPlayer = PlayerUtils.getPlayer(playerId);
		guildMemberBiz.refusePlayerReq(guild, reqPlayer);
		guildBiz.sendReqMember(player, guild);
		guild.saveDB();
		player.sendMsg(MessageComm.S2C_Guild_RefusePlayer);
	}

	//同意加入部落申请
	@MsgMethod(MessageComm.C2S_Guild_Agree)
	public void agree(Player player, Guild guild, JsonMsg msg) {
		long playerId = msg.getInt("playerId");
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		
		//校验部落人数上限
		if(guildMemberBiz.isGuildMemberMax(guild)) {
			player.sendErrorMsg(SysConstant.Guild_PlayerNumErr);
			return ;
		}
		//获取要申请加入的玩家
		GuildPlayer reqPlayer = guild.getGuildReqs().guildPlayer(playerId);
		if(null==reqPlayer) {
			player.sendErrorMsg(SysConstant.Guild_JoinErr);
			return ;
		}
		Player reqBasePlayer = PlayerUtils.getPlayer(playerId);

		List<Integer> reqList = reqBasePlayer.playerGuild().getReqGuild();
		if(!reqList.contains(guild.getId())) {
			player.sendErrorMsg(SysConstant.Guild_NoneReq);
			return ;
		}
		guildMemberBiz.agree(player, reqBasePlayer, guild);
		log.error("同意部落成员加入：{}", playerId);
		guildBiz.sendReqMember(player, guild);
		guildBiz.sendGuildMember(player);
		player.sendMsg(MessageComm.S2C_Guild_Agree);
	}
	
	//部落邀请用户加入部落信息  #msg:30035,tarPlayerId=100013
	@MsgMethod(MessageComm.C2S_Guild_Invite)
	public void invitePlayer(Player player, Guild guild, JsonMsg msg) {
		//被邀请人的id
		int tarPlayerId = msg.getInt("tarPlayerId");
		//校验是否有权限（部落长，副部落长能邀请）
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		
		Map<Long, GuildInviteBean> listPlayerId = guild.getGuildInvite().getInviteGuild();
		//清理以前邀请过的玩家。
		guildBiz.cleanInvite(guild, listPlayerId);
        //获取用户，判断是否在线
  		Player tarPlayer = PlayerContainer.getPlayer(tarPlayerId);
  		if(null==tarPlayer) {
  			player.sendErrorMsg(SysConstant.Guild_OffLine);
  			return ;
  		}
		if(listPlayerId.containsKey(tarPlayer.getId())) {
			player.sendErrorMsg(SysConstant.Guild_InviteRepeat);
			return ;
		}
		if(tarPlayer.getGuildId()>0) {
			player.sendErrorMsg(SysConstant.Guild_InGuild);
			return ;
		}
		if(!GuildCheckUtil.checkIsOpen(tarPlayer)) {
			player.sendErrorMsg(SysConstant.Guild_Close);
			return ;
		}
		int leaveTime = commValueConfig.getCommValue(CommonValueType.leaveTime);
		if(!tarPlayer.playerGuild().timeCheck(leaveTime)) {
			player.sendErrorMsg(SysConstant.Guild_TarLeaveTime);
			return ;
		}
		//玩家开启部落邀请拒绝功能
		if(!tarPlayer.playerSet().isClose(UserSetTypeEnum.GuildReq)) {
			player.sendErrorMsg(SysConstant.Guild_Refuse);
			return ;
		}
		guild.getGuildInvite().addInvite(tarPlayerId, player.getId());
		guild.saveDB();
		tarPlayer.sendMsg(MessageComm.S2C_Guild_Invite, GuildVo.buildVo(guild));
	}

	//自己退出部落
	@MsgMethod(MessageComm.C2S_Guild_Quit)
    public void quit(Player player, Guild guild, JsonMsg msg) {
		guildMemberBiz.quit(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Guild_Quit, true);
	}
	
}
