package com.hm.action.guild;

import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.util.GuildCheckUtil;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.SensitiveWordUtil;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Action
public class GuildAction extends AbstractGuildAction{
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private LogBiz logBiz;

	//获取自己所在部落信息
	@MsgMethod(MessageComm.C2S_Guild_Get)
    public void getGuild(Player player, Guild guild, JsonMsg msg) {
		guildBiz.sendGuildMember(player);
		guild.sendPlayerAllGuild(player);
	}

	//获取自己部落申请列表
	@MsgMethod(MessageComm.C2S_Guild_GetReq)
    public void getReqMember(Player player, Guild guild, JsonMsg msg) {
		guildBiz.sendReqMember(player, guild);
	}
	
	//转让部落
	@MsgMethod(MessageComm.C2S_Guild_Trans)
    public void transfer(Player player, Guild guild, JsonMsg msg) {
		long playerId = msg.getInt("playerId");
		if(!guild.isLeader(player) ) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		Player transPlayer = PlayerUtils.getPlayer(playerId);
		if(null==transPlayer) {
			player.sendErrorMsg(SysConstant.PLAYER_NOT_EXIST);
			return ;
		}
		boolean result = guildBiz.transfer(player, guild, transPlayer);
		log.error("部落长转让职务："+ player.getId() + ";转让给："+transPlayer.getId());
		logBiz.addPlayerActionLog(player, ActionType.GuildTrans.getCode(), player.getId()+"_"+transPlayer.getId());
		guildBiz.sendFailMail(guild, player, null);
		guildBiz.sendGuildMember(player);
		player.sendMsg(MessageComm.S2C_Guild_Trans, result);
	}

	//修改部落公告信息
	@MsgMethod(MessageComm.C2S_Guild_ChangeNotice)
    public void changeNotice(Player player, Guild guild,  JsonMsg msg) {
		String notice = msg.getString("notice");
		notice = SensitiveWordUtil.replaceSensitiveWord(notice, "*");
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		guildBiz.changeNotice(guild, player, notice);
		//发送部落信息
		guild.sendPlayerGuild(player);
		player.sendMsg(MessageComm.S2C_Guild_ChangeNotice);
	}


	/**
	 * impeachLeader:(弹劾部落长). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
	@MsgMethod(MessageComm.C2S_Guild_Impeach)
    public void impeachLeader(Player player, Guild guild, JsonMsg msg) {
		int checkResult = 0;
		int time = commValueConfig.getCommValue(CommonValueType.GuildImpeach);
		//校验是否符合弹劾条件
		if((checkResult = GuildCheckUtil.checkCanImpleach(player, guild, time))!=-1) {
			player.sendErrorMsg(checkResult);
			return;
		}
		//扣除资源
		List<Items> costs = commValueConfig.getListItem(CommonValueType.GuildImpeachCoast);
		if(!itemBiz.checkItemEnoughAndSpend(player, costs, LogType.GuildImpeachCost)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		int lastTime = commValueConfig.getCommValue(CommonValueType.GuildImpeachLast);
		//开始弹劾	
		guild.getGuildImpeach().startImpeach(player.getId(), lastTime);
		//广播个部落其他在线玩家
		guild.broadMemberGuildUpdate();
		guild.saveDB();
		log.error("{}弹劾部落{}的部落首领{}:", player.getId(), guild.getGuildInfo().getGuildName(), guild.getGuildInfo().getLeaderId());
		player.sendMsg(MessageComm.S2C_Guild_Impeach, ImpeachResult.Normal.getType());
		player.sendUserUpdateMsg();
	}

	/**
	 * impeachDeal:(处理部落弹劾，是否同意弹劾). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
	@MsgMethod(MessageComm.C2S_Guild_Deal)
    public void impeachDeal(Player player, Guild guild, JsonMsg msg) {
		//是否同意弹劾部落长，对应GuildImpeachStateEnum中的type
		int type = msg.getInt("type");
		if(!GuildImpeachStateEnum.isVoteType(type)) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		//判断是否已经结束
		if(guild.getGuildImpeach().isEnd()) {
			player.sendErrorMsg(SysConstant.Guild_ImpeachCondition);
			return;
		}
		//是否已经参与弹劾
		if(guild.getGuildImpeach().hasImpeach(player.getId())) {
			player.sendErrorMsg(SysConstant.Guild_ImpeachRep);
			return;
		}
		guild.getGuildImpeach().addImpeach(player.getId(), GuildImpeachStateEnum.get(type));
		guild.saveDB();
		//广播个部落其他在线玩家
		guild.broadMemberGuildUpdate();
		player.sendMsg(MessageComm.S2C_Guild_Deal, ImpeachResult.Normal.getType());
	}

	@MsgMethod(MessageComm.C2S_Guild_AutoAdd)
    public void autoAdd(Player player, Guild guild, JsonMsg msg) {
		int autoAdd = msg.getInt("autoAdd");
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return;
		}
		guild.changeAutoAdd(autoAdd);
		guild.sendManagerGuildUpdate();
		player.sendMsg(MessageComm.S2C_Guild_AutoAdd, guild.getAutoAdd());
	}
}













