package com.hm.action.guild;

import com.hm.action.guild.biz.GuildFlagBiz;
import com.hm.action.guild.biz.GuildFlagManger;
import com.hm.action.guild.util.GuildCheckException;
import com.hm.action.guild.util.GuildCheckUtil;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.StatisticsType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.util.HFUtils;

import javax.annotation.Resource;

@Action
public class GuildFlagAction extends AbstractGuildAction{
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private GuildFlagBiz guildFlagBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private PlayerBiz playerBiz;
	
	//修改部落名字 #msg:30021,name=123456
	@MsgMethod(MessageComm.C2S_Guild_ChangeName)
    public void changeName(Player player, Guild guild, JsonMsg msg) {
		String guildName = msg.getString("name");
		//是否有权限修改
		if(!guild.isLeader(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return;
		}
		// 合服服务器用户名字自动加上服务器前缀
		String trueGuildName = HFUtils.checkName(guild, guildName);
		try {
			if (guild.getGuildInfo().getGuildName().equals(guildName)){
				return;
			}
			GuildCheckUtil.checkName(player, guildName);
			GuildCheckUtil.checkNameRepeat(player, trueGuildName);

			if(!itemBiz.checkItemEnoughAndSpend(player, guildFlagBiz.getChangeNameCost(), LogType.GuildChangeName)) {
				player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
				return;
			}
			guildFlagBiz.changeGuildName(guild, trueGuildName);
			guild.broadMemberGuildUpdate();
			guild.saveDB();
			player.sendUserUpdateMsg();
			player.sendMsg(MessageComm.S2C_Guild_ChangeName);
		} catch (GuildCheckException e) {
			player.sendErrorMsg(e.getErrorCode());
		}
	}

	//修改部落旗帜
	@MsgMethod(MessageComm.C2S_Guild_ChangeFlag)
	public void changeFlag(Player player, Guild guild, JsonMsg msg) {
		String guildFlag = msg.getString("guildFlag");
		//是否有权限修改
		if(!guild.isLeader(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return;
		}
		if (!GuildFlagManger.getFlagList().contains(guildFlag)){
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		if(guild.getGuildFlag().getFlagName().equals(guildFlag)) {
			return;
		}
		try {
			GuildCheckUtil.checkFlagRepeat(player, guildFlag);
			long changeCount = guild.getGuildStatistics().getLifeStatistics(StatisticsType.GuildChangeFlag);
			int cost = commValueConfig.getCommValue(CommonValueType.ChangeGuildFlagCost);
			if(changeCount > 0 && !playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, cost, LogType.GuildChangeName)) {
				player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
				return;
			}
			guildFlagBiz.changeGuildFlag(guild, guildFlag);
			guild.getGuildStatistics().addLifeStatistics(StatisticsType.GuildChangeFlag);
			guild.broadMemberGuildUpdate();
			guild.saveDB();
			player.sendUserUpdateMsg();
			player.sendMsg(MessageComm.S2C_Guild_ChangeFlag);
		} catch (GuildCheckException e) {
			player.sendErrorMsg(e.getErrorCode());
		}
	}
}
