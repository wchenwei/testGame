package com.hm.action.robsupply;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.robsupply.biz.NpcRobSupply;
import com.hm.action.robsupply.biz.RobSupplyBiz;
import com.hm.action.robsupply.biz.SupplyTroopVo;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.db.PlayerUtils;
import com.hm.db.WarResultUtils;
import com.hm.enums.*;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.troop.CityDefNpcTroop;
import com.hm.model.fight.FightProxy;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.SupplyItem;
import com.hm.model.supplytroop.SupplyTroop;
import com.hm.model.war.BattleRecord;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.supplyTroop.SupplyTroopServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.SupplyPlayerFightTroop;
import com.hm.war.sg.troop.TankArmy;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
@Action
public class RobSupplyAction extends AbstractPlayerAction{
	@Resource
	private RobSupplyBiz robSupplyBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private ItemBiz itemBiz;
	
	@MsgMethod(MessageComm.C2S_SupplyRefresh)
	public void supplyRefresh(Player player,JsonMsg msg) {
		//运送次数用完了
		double dayMaxNum = commValueConfig.getLvModeValue(CommonValueType.SupplyMaxNum, player.playerLevel().getLv());
		if(player.getPlayerStatistics().getTodayStatistics(StatisticsType.SupplyTroop)
				>= dayMaxNum) {
			return;
		}
		int spend = commValueConfig.getCommValue(CommonValueType.SupplyRefresh);
		if(!player.playerRobSupply().isFreeRefresh()){
			spend = 0;
		}
		if(spend > 0 && !playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, spend, LogType.SupplyRefresh)) {
			player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}
		robSupplyBiz.refershSupplyList(player);
		if(spend <= 0){
			player.playerRobSupply().setFreeRefresh(true);
		}
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_SupplyRefresh);
	}
	
	@MsgMethod(MessageComm.C2S_SupplyTroopStart)
	public void supplyStart(Player player,JsonMsg msg) {
		String id = msg.getString("id");
		double dayMaxNum = commValueConfig.getLvModeValue(CommonValueType.SupplyMaxNum, player.playerLevel().getLv());
		//判断次数
		if(player.getPlayerStatistics().getTodayStatistics(StatisticsType.SupplyTroop)
				>= dayMaxNum) {
			return;
		}
		SupplyItem supplyItem = player.playerRobSupply().getSupplyItem(id);
		if(supplyItem == null || supplyItem.getType() != SupplyTroopType.None.getType()) {
			return;//选择城池异常
		}
		//创建时已将坦克池中的血量同步到部队中
		ClientTroop clientTroop = ClientTroop.buildFull(player, "armys", msg);
		//ArrayList<TankArmy> armyList = robSupplyBiz.createTankArmys(msg.getString("armys"),player);
		List<TankArmy> armyList = clientTroop.getArmyList();
		if(!GameConstants.checkTroopSize(armyList.size())) {
			player.sendErrorMsg(SysConstant.Supply_TankNumError);//小于5 不能出征
			return;
		}
		if(!robSupplyBiz.checkTankArmyState(player, null ,armyList)) {
			player.sendErrorMsg(SysConstant.WorldTroop_TankStateError);//检查坦克状态
			return;
		}
		//派遣坦克列表
		List<Integer> tankIds = armyList.stream().map(e -> e.getId()).collect(Collectors.toList());
		long moveSpeed = commValueConfig.getCommValue(CommonValueType.SupplySpeed)*GameConstants.MINUTE;
		long endTime = System.currentTimeMillis()+supplyItem.getWaySize()*moveSpeed;
		//更改为运行中
		supplyItem.changeSupplyType(SupplyTroopType.Run);
		supplyItem.setEndTime(endTime);
		supplyItem.loadDouble(isDouble());
		player.playerRobSupply().addDispatchTank(tankIds);//添加派遣坦克列表
		player.playerRobSupply().SetChanged();
		//组件世界部队
		SupplyTroop supplyTroop = new SupplyTroop();
		supplyTroop.createLoadPlayer(player, supplyItem,endTime);
		supplyTroop.loadDefTroop(clientTroop);
		SupplyTroopServerContainer.of(player).addSupplyTroop(supplyTroop);
		player.getPlayerStatistics().addTodayStatistics(StatisticsType.SupplyTroop);
		player.notifyObservers(ObservableEnum.SupplyTroop);
		
		player.sendUserUpdateMsg();
		//开始运输成功
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_SupplyTroopStart);
        showMsg.addProperty("addTroop", new SupplyTroopVo(supplyTroop));
        player.sendMsg(showMsg);
        log.error(player.getId()+"护送开始");
	}
	
	private static boolean isDouble() {
		int hour = DateUtil.thisHour(true);
		return hour == 12;
	}
	
	@MsgMethod(MessageComm.C2S_SupplyReward)
	public void supplyReward(Player player,JsonMsg msg) {
		String id = msg.getString("id");
		SupplyItem supplyItem = player.playerRobSupply().getSupplyItem(id);
		if(supplyItem == null || supplyItem.getType() != SupplyTroopType.Over.getType()) {
			return;//还没有结束
		}
		//删除现在的
		player.playerRobSupply().removeSupplyItem(id);
		Items supplyReward = supplyItem.getSupplyItem();
		//护送奖励
		itemBiz.addItem(player, supplyReward, LogType.SupplyItem);
		player.sendUserUpdateMsg();
		//展示奖励
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_SupplyReward);
        showMsg.addProperty("itemList", Lists.newArrayList(supplyReward));
        player.sendMsg(showMsg);
	}
	
	@MsgMethod(MessageComm.C2S_OpenSupply)
	public void openSupply(Player player,JsonMsg msg) {
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.RobSupply)) {
			return;
		}
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_OpenSupply);
        showMsg.addProperty("enemyList", robSupplyBiz.createSupplyEnemyList(player));
        player.sendMsg(showMsg);
	}
	
	@MsgMethod(MessageComm.C2S_SupplyRefrshEnemy)
	public void supplyRefrshEnemy(Player player,JsonMsg msg) {
		//抢劫次数用完了
		int dayMaxNum = commValueConfig.getCommValue(CommonValueType.SupplyRobMaxNum);
		if(player.getPlayerStatistics().getTodayStatistics(StatisticsType.SupplyRobTroop)
				>= dayMaxNum) {
			return;
		}
		int spend = commValueConfig.getCommValue(CommonValueType.SupplyEnemyRefresh);
		if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Cash, spend, LogType.SupplyRefreshEnemy)) {
			player.sendErrorMsg(SysConstant.PLAYER_Cash_NOT);
			return;
		}
        robSupplyBiz.refreshSupplyEnemyList(player);
        
		List<SupplyTroopVo> voList = robSupplyBiz.createSupplyEnemyList(player);
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_SupplyRefrshEnemy);
        showMsg.addProperty("enemyList", voList);
        player.sendMsg(showMsg);
        
        player.sendUserUpdateMsg();
	}
	
	@MsgMethod(MessageComm.C2S_SupplyRob)
	public void supplyRob(Player player,JsonMsg msg) {
		String id = msg.getString("id");
		if(robSupplyBiz.isNpc(id)) {
			supplyNpcRob(player, msg);
			return;
		}
		SupplyTroop supplyTroop = SupplyTroopServerContainer.of(player).getSupplyTroop(id);
		if(supplyTroop == null || player.getId() == supplyTroop.getPlayerId()) {
			player.sendErrorMsg(SysConstant.Supply_RobTroopOver);
			return;
		}
		if(supplyTroop.isRob()) {
			player.sendErrorMsg(SysConstant.Supply_TroopHaveRob);
			return;
		}
		//判断次数
		int dayMaxNum = commValueConfig.getCommValue(CommonValueType.SupplyRobMaxNum);
		if(player.getPlayerStatistics().getTodayStatistics(StatisticsType.SupplyRobTroop)
				>= dayMaxNum) {
			return;
		}
		//创建时已将坦克池中的血量同步到部队中
		ClientTroop clientTroop = ClientTroop.buildFull(player, "armys", msg);
//		ArrayList<TankArmy> armyList = robSupplyBiz.createTankArmys(msg.getString("armys"),player);
		List<TankArmy> armyList = clientTroop.getArmyList();
		if(!GameConstants.checkTroopSize(armyList.size())) {
			player.sendErrorMsg(SysConstant.Supply_TankNumError);//小于5 不能出征
			return;
		}
		if(!robSupplyBiz.checkTankArmyState(player, null ,armyList)) {
			player.sendErrorMsg(SysConstant.WorldTroop_TankStateError);//检查坦克状态
			return;
		}
		//添加防御方
		Player defPlayer = PlayerUtils.getPlayer(supplyTroop.getPlayerId());
		SupplyPlayerFightTroop atkTroop = new SupplyPlayerFightTroop(player, clientTroop);
		SupplyPlayerFightTroop defTroop = new SupplyPlayerFightTroop(supplyTroop);
		WarResult warResult = new FightProxy().doFight(atkTroop, defTroop);
		warResult.setWarResultType(WarResultType.SupplyRob);
		if(warResult.isAtkWin()) {
			//计算掠夺奖励
			List<Items> rewardList = Lists.newArrayList();
			SupplyItem supplyItem = defPlayer.playerRobSupply().getSupplyItem(id);
			if(supplyItem != null) {
				supplyTroop.setRob(true);
				supplyItem.doRob();//处理被打劫
				rewardList.add(supplyItem.getRobItem());//获取打劫的物品
				//抢劫物品
				itemBiz.addItem(player, rewardList, LogType.SupplyRob);
				warResult.setShowRewardList(rewardList);
			}
			player.getPlayerStatistics().addTodayStatistics(StatisticsType.SupplyRobTroop);
		}
		syncTankHp(player,defPlayer,warResult);//同步坦克血量
		
		supplyTroop.saveDB();//记录部队的剩余状态
		BattleRecord record = new BattleRecord(player.getServerId(), warResult);
		WarResultUtils.saveOrUpdate(record);
		
		player.playerRobSupply().addRecordLog(record.getId());
		player.sendUserUpdateMsg();
		
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_SupplyRob);
        showMsg.addProperty("battleRecord", warResult);
        showMsg.addProperty("enemyList", robSupplyBiz.createSupplyEnemyList(player));
        player.sendMsg(showMsg);
		
		defPlayer.playerRobSupply().addRecordLog(record.getId());
		defPlayer.sendUserUpdateMsg();
	}
	
	public void supplyNpcRob(Player player,JsonMsg msg) {
		String id = msg.getString("id");
		NpcRobSupply npcRobSupply = player.playerRobSupply().getNpcRobSupply(id);
		//判断次数
		int dayMaxNum = commValueConfig.getCommValue(CommonValueType.SupplyRobMaxNum);
		if(player.getPlayerStatistics().getTodayStatistics(StatisticsType.SupplyRobTroop)
				>= dayMaxNum) {
			return;
		}
		//创建时已将坦克池中的血量同步到部队中
		ClientTroop clientTroop = ClientTroop.buildFull(player, "armys", msg);
		//ArrayList<TankArmy> armyList = robSupplyBiz.createTankArmys(msg.getString("armys"),player);
		List<TankArmy> armyList = clientTroop.getArmyList();
		if(!GameConstants.checkTroopSize(armyList.size())) {
			player.sendErrorMsg(SysConstant.Supply_TankNumError);//小于5 不能出征
			return;
		}
		if(!robSupplyBiz.checkTankArmyState(player, null ,armyList)) {
			player.sendErrorMsg(SysConstant.WorldTroop_TankStateError);//检查坦克状态
			return;
		}
		//添加防御方
		AbstractFightTroop atkTroop = new SupplyPlayerFightTroop(player, clientTroop);
		AbstractFightTroop defTroop = new CityDefNpcTroop(npcRobSupply.getNpcId(), player.getServerId(), 0).createNpcTroop();
		WarResult warResult = new FightProxy().doFight(atkTroop, defTroop);
		warResult.setWarResultType(WarResultType.SupplyRob);
		if(warResult.isAtkWin()) {
			//计算掠夺奖励
			List<Items> rewardList = Lists.newArrayList();
			SupplyItem supplyItem = npcRobSupply.getSupplyItem();
			if(supplyItem != null) {
				supplyItem.doRob();//处理被打劫
				rewardList.add(supplyItem.getRobItem());//获取打劫的物品
				//抢劫物品
				itemBiz.addItem(player, rewardList, LogType.SupplyRob);
				warResult.setShowRewardList(rewardList);
			}
			player.getPlayerStatistics().addTodayStatistics(StatisticsType.SupplyRobTroop);
		}
		
		BattleRecord record = new BattleRecord(player.getServerId(), warResult);
		WarResultUtils.saveOrUpdate(record);
		
		player.playerRobSupply().removeNpcRobSupply(id);
		player.playerRobSupply().addRecordLog(record.getId());
		player.sendUserUpdateMsg();
		
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_SupplyRob);
        showMsg.addProperty("battleRecord", warResult);
        showMsg.addProperty("enemyList", robSupplyBiz.createSupplyEnemyList(player));
        player.sendMsg(showMsg);
	}
	
	@Deprecated
	private void syncTankHp(Player atkPlayer,Player defPlayer,WarResult warResult) {
		AbstractFightTroop atkTroop = warResult.getAtk().getFightTroop();
		AbstractFightTroop defTroop = warResult.getDef().getFightTroop();
		if(warResult.isAtkWin()) {
			atkTroop.doWarResult(warResult.getAtk());
			defTroop.getTankList().forEach(e -> e.setHp(0));
		}else{
			defTroop.doWarResult(warResult.getDef());
			atkTroop.getTankList().forEach(e -> e.setHp(0));
		}
//		atkPlayer.playerRobSupply().syncTankHp(atkTroop.getTankList());
//		defPlayer.playerRobSupply().syncTankHp(defTroop.getTankList());
	}
	
	
	@MsgMethod (MessageComm.C2S_SupplyRecord)
	public void recordList(Player player, JsonMsg msg){
		List<BattleRecord> battRecordList = player.playerRobSupply().getRecordList()
				.stream().map(e -> WarResultUtils.getBattleRecord(player.getServerId(), e))
				.filter(Objects::nonNull).collect(Collectors.toList()); 
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_SupplyRecord);
		serverMsg.addProperty("recordList", battRecordList);
		player.sendMsg(serverMsg);
	}
	
}
