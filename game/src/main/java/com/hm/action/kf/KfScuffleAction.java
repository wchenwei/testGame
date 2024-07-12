package com.hm.action.kf;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.kf.vo.KfKingPlayerInfo;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.ActivityType;
import com.hm.enums.CommonValueType;
import com.hm.enums.KfType;
import com.hm.message.MessageComm;
import com.hm.model.activity.kfactivity.KfScuffleActivity;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.redis.troop.TroopRedisBiz;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 跨服乱斗
 * @author xjt  
 * @date 2020年11月10日15:52:31
 * @version V1.0
 */
@Action
public class KfScuffleAction extends AbstractPlayerAction{
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private ActivityBiz activityBiz;

	@Resource
    private TroopRedisBiz troopRedisBiz;
	
	@MsgMethod(MessageComm.C2S_GetSuffleSignInfo)
    public void getSuffleInfo(Player player, JsonMsg msg) {
		KfScuffleActivity activity = (KfScuffleActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFScuffle);
		if(activity == null || !activity.isOpen()) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		sendSignPlayerInfo(player,activity);
	}
	
	@MsgMethod(MessageComm.C2S_KfScuffleSignup)
    public void signup(Player player, JsonMsg msg) {
		int minLevel = commValueConfig.getCommValue(CommonValueType.KFScuffle_MinLv);
		long playerId = player.getId();
		KfScuffleActivity activity = (KfScuffleActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFScuffle);
		if(activity == null || !activity.isOpen()) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		if(!activity.isSignupTime()) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		if(player.playerLevel().getLv() < minLevel) {
			player.sendErrorMsg(SysConstant.Player_Level_Not_Enough);
			return;
		}
		if(activity.getJoinIds().contains(playerId)) {
			return;//你已经报过名了
		}
		activity.sign(playerId);
		activity.saveDB();
		sendSignPlayerInfo(player,activity);
		activity.loadKfServer();
		player.sendMsg(MessageComm.S2C_KfScuffleSignup);
		player.notifyObservers(ObservableEnum.KfScuffleSignup, KfType.KfScuffle);
	}
	
	public void sendSignPlayerInfo(Player player,KfScuffleActivity activity) {
		List<KfKingPlayerInfo> playerList = Lists.newArrayList();
		List<Long> playerIds = activity.getJoinIds();
		for (long id:playerIds) {
			playerList.add(new KfKingPlayerInfo(id));
		}
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_GetSuffleSignInfo);
		serverMsg.addProperty("playerList", playerList);
		player.sendMsg(serverMsg);
	}
	
}
