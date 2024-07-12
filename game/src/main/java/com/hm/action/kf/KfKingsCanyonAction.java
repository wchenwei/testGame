package com.hm.action.kf;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.kf.vo.KfKingPlayerInfo;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.ActivityType;
import com.hm.enums.CommonValueType;
import com.hm.enums.KfType;
import com.hm.message.MessageComm;
import com.hm.model.activity.kfactivity.KfKingsCanyonActivity;
import com.hm.model.activity.kfactivity.KfSignPlayer;
import com.hm.model.fight.FightProxy;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.redis.troop.TroopRedisBiz;
import com.hm.redis.troop.TroopRedisType;
import com.hm.redis.troop.TroopRedisUtils;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.PlayerTroop;
import com.hm.war.sg.troop.TankArmy;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 王者峡谷
 * @author siyunlong  
 * @date 2020年3月31日 下午12:12:43 
 * @version V1.0
 */
@Action
public class KfKingsCanyonAction extends AbstractPlayerAction{
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private ActivityBiz activityBiz;

	@Resource
    private TroopRedisBiz troopRedisBiz;
	
	@MsgMethod(MessageComm.C2S_KfKingChangeDef)
    public void kingChangeDef(Player player, JsonMsg msg) {
		KfKingsCanyonActivity kingsCanyonActivity = (KfKingsCanyonActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFKingsCanyon);
		if(kingsCanyonActivity == null || !kingsCanyonActivity.isOpen()) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		if(!kingsCanyonActivity.isSignupTime()) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		KfSignPlayer oppoSignPlayer = kingsCanyonActivity.getPlayerById(player.getId());
		if(oppoSignPlayer == null) {
			return;
		}
		ClientTroop clientTroop = ClientTroop.buildFull(player, "armys", msg);
		//创建时已将坦克池中的血量同步到部队中
		List<TankArmy> armyList = clientTroop.getArmyList();
		if(!GameConstants.checkTroopSize(armyList.size())) {
			return;
		}
		oppoSignPlayer.setTroopInfo(clientTroop);
		kingsCanyonActivity.saveDB();
		player.sendMsg(MessageComm.S2C_KfKingChangeDef);
	}
	
	@MsgMethod(MessageComm.C2S_getKingInfo)
    public void getKingInfo(Player player, JsonMsg msg) {
		KfKingsCanyonActivity kingsCanyonActivity = (KfKingsCanyonActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFKingsCanyon);
		if(kingsCanyonActivity == null || !kingsCanyonActivity.isOpen()) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		sendKingPlayerInfo(player,kingsCanyonActivity);
	}
	
	@MsgMethod(MessageComm.C2S_KfKingSignup)
    public void kingSignup(Player player, JsonMsg msg) {
		int minLevel = commValueConfig.getCommValue(CommonValueType.KfKingCanyonLv);
		long playerId = player.getId();
		KfKingsCanyonActivity kingsCanyonActivity = (KfKingsCanyonActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFKingsCanyon);
		if(kingsCanyonActivity == null || !kingsCanyonActivity.isOpen()) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		if(!kingsCanyonActivity.isSignupTime()) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		if(player.playerLevel().getLv() < minLevel) {
			player.sendErrorMsg(SysConstant.Player_Level_Not_Enough);
			return;
		}
		if(kingsCanyonActivity.getPlayerById(playerId) != null) {
			return;//你已经报过名了
		}
		int index = msg.getInt("index");
		//创建时已将坦克池中的血量同步到部队中
		ClientTroop clientTroop = ClientTroop.buildFull(player, "armys", msg);

		if(!GameConstants.checkTroopSize(clientTroop.getArmyList().size())) {
			return;
		}
		KfSignPlayer oppoSignPlayer = kingsCanyonActivity.getPlayerByIndex(index);
		if(oppoSignPlayer != null) {
			//战斗
			PlayerTroop atkTroop = new PlayerTroop(player.getId(), "kingfight");
			atkTroop.loadClientTroop(clientTroop);
			PlayerTroop defTroop = new PlayerTroop(oppoSignPlayer.getId(), "kingfight");
			ClientTroop defClientTroop = oppoSignPlayer.getTroopInfo();
			defTroop.loadClientTroop(defClientTroop);
			
			WarResult warResult = new FightProxy().doFight(atkTroop, defTroop);
			
			JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_KfKingSignup);
			serverMsg.addProperty("battleRecord", warResult);
			player.sendMsg(serverMsg);
			if(!warResult.isAtkWin()) {
				return;
			}
			TroopRedisUtils.delKey(TroopRedisType.KfWz, oppoSignPlayer.getId());
		}
		kingsCanyonActivity.setPlayerIdByIndex(index, new KfSignPlayer(playerId, clientTroop));
		kingsCanyonActivity.saveDB();
		
		troopRedisBiz.changeRedisTroop(TroopRedisType.KfWz, playerId, clientTroop.getArmyList());
		
		if(oppoSignPlayer == null) {
			sendKingPlayerInfo(player,kingsCanyonActivity);
		}
		kingsCanyonActivity.loadKfServer();
		player.notifyObservers(ObservableEnum.KfKingSignup, KfType.KfKingCanyon);
	}
	
	public void sendKingPlayerInfo(Player player,KfKingsCanyonActivity kingsCanyonActivity) {
		List<KfKingPlayerInfo> playerList = Lists.newArrayList();
		KfSignPlayer[] playerIds = kingsCanyonActivity.getPlayerIds();
		for (int i = 0; i < playerIds.length; i++) {
			KfSignPlayer signPlayer = playerIds[i];
			if(signPlayer != null) {
				playerList.add(new KfKingPlayerInfo(signPlayer, i));
			}
		}
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KfKingPlayersUpdate);
		serverMsg.addProperty("playerList", playerList);
		player.sendMsg(serverMsg);
	}
	
}
