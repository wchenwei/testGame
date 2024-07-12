package com.hm.action.troop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.bag.BagBiz;
import com.hm.action.captive.CaptiveBiz;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildCityFightBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.action.troop.biz.TroopFightBiz;
import com.hm.action.troop.biz.WorldTroopBiz;
import com.hm.action.troop.client.ClientTroop;
import com.hm.action.troop.vo.FullTroopResult;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.ItemConfig;
import com.hm.enums.*;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.string.StringUtil;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.AirdropTatics;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.ItemConstant;
import com.hm.sysConstant.SysConstant;
import com.hm.timerjob.GuildWarUtils;
import com.hm.war.sg.troop.TankArmy;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Action
public class   TroopAction extends AbstractPlayerAction{
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private TroopFightBiz troopFightBiz;
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private BagBiz bagBiz;
	@Resource
	private GuildTechBiz guildTechBiz;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private WorldTroopBiz worldTroopBiz;
	@Resource
    private ItemBiz itemBiz;
	@Resource
    private CaptiveBiz captiveBiz;
	@Resource
	private GuildCityFightBiz guildCityFightBiz;
	@Resource
	private CityConfig cityConfig;
	/**
	 * 创建世界部队
	 * @param player
	 * @param msg
	 */
	@MsgMethod(MessageComm.C2S_CreateWorldTroop)
	public void createWorldTroop(Player player,JsonMsg msg) {
		//判断部队数量
		int playerTroopSize = player.playerTroops().getTroopSize();
		if(playerTroopSize >= commValueConfig.getTroopNum(player.playerMission().getFbId())) {
			//大于最大部队数量了
			player.sendErrorMsg(SysConstant.WorldTroop_MaxNum);
			return;
		}

		ClientTroop clientTroop = ClientTroop.build(player, "armys", msg);
		//创建时已将坦克池中的血量同步到部队中
		List<TankArmy> armyList = clientTroop.getArmyList();
		if(!GameConstants.checkTroopSize(armyList.size())) {
			//小于5 不能出征
			player.sendErrorMsg(SysConstant.WorldTroop_TankNumError);
			return;
		}
		if(!troopBiz.checkTankArmyState(player, null ,armyList)) {
			//检查坦克状态
			player.sendErrorMsg(SysConstant.WorldTroop_TankStateError);
			return;
		}
		if(!TroopBiz.checkGameFightTankArmy(player, armyList)){
			player.sendErrorMsg(SysConstant.TankType_Not);
			return;
		}
		worldTroopBiz.createPlayerTroop(player,clientTroop);
		//发送客户端添加队伍
		player.sendWorldTroopMessage();
		player.sendUserUpdateMsg();

		player.sendMsg(MessageComm.S2C_CreateWorldTroop);
	}
	
	@MsgMethod(MessageComm.C2S_EditorWorldTroop)
	public void editorWorldTroop(Player player,JsonMsg msg) {
		String troopId = msg.getString("troopId");
		if(!worldTroopBiz.isCanOperationTroop(player.getServerId(),troopId)){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}
		if(!player.playerTroops().isContain(troopId)) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorNot);
			return;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorNot);
			return;
		}
		if(!troopBiz.worldTroopIsCanEditor(player, worldTroop)) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorTankStateNot);
			return;
		}
		//将下阵的坦克血量同步到坦克池中
		troopBiz.changeHpToTank(worldTroop, player);
		ClientTroop clientTroop = ClientTroop.build(player, "armys", msg);
		List<TankArmy> armyList = clientTroop.getArmyList();
		if(captiveBiz.isCanCaptiveChangeTank(player, worldTroop, armyList)) {
			player.sendErrorMsg(SysConstant.WorldTroop_HaveCaptiveTank);
			return;
		}
		if(!troopBiz.worldTroopIsCanEditor(player, worldTroop)) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorTankStateNot);
			return;
		}
		if(!TroopBiz.checkGameFightTankArmy(player, armyList)){
			player.sendErrorMsg(SysConstant.TankType_Not);
			return;
		}
		if(!troopBiz.checkTankArmyState(player, troopId ,armyList)) {//检查坦克状态
			player.sendErrorMsg(SysConstant.WorldTroop_TankStateError);
			return;
		}
		worldTroop.loadClientTroop(clientTroop);
		worldTroop.saveDB();
		//发送客户端添加队伍
		player.saveDB();
		player.notifyObservers(ObservableEnum.TroopChange);
		player.sendUserUpdateMsg();
		troopBiz.sendWorldTroopUpdate(player, worldTroop);

		player.sendMsg(MessageComm.S2C_EditorWorldTroop);
	}
	//解散
	@MsgMethod(MessageComm.C2S_DisbandWorldTroop)
	public void disbandWorldTroop(Player player,JsonMsg msg) {
		String troopId = msg.getString("troopId");
		if(!player.playerTroops().isContain(troopId)) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorNot);
			return;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorNot);
			return;
		}
		if(!worldTroopBiz.isCanOperationTroop(worldTroop)){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}
		if(!troopBiz.worldTroopIsCanEditor(player, worldTroop)) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorTankStateNot);
			return;
		}
		if(captiveBiz.haveCaptiveTank(player, worldTroop)) {
			player.sendErrorMsg(SysConstant.WorldTroop_HaveCaptiveTank);//有俘虏tank
			return;
		}
		//删除部队
		troopBiz.removeWorldTroop(worldTroop,player);

		player.sendMsg(MessageComm.S2C_DisbandWorldTroop);
	}
	
	//派遣部队攻去某个城市
	@MsgMethod(MessageComm.C2S_DispatchWorldTroop)
	public void dispatchWorldTroop(Player player,JsonMsg msg) {
		String troopId = msg.getString("troopId");
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null) {
			return;//部队不存在
		}
		if(!player.isKFPlayer() && !GuildWarUtils.isOpenWar(player.getServerId())) {
			player.sendErrorMsg(SysConstant.WarTimeNot);
			return;
		}
		if(player.getGuildId() <= 0) {
			player.sendErrorMsg(SysConstant.WarNot_NoGuild);
			return;
		}
		if(!worldTroopBiz.isCanOperationTroop(worldTroop) || worldTroopBiz.isTroopIsBusy(worldTroop)){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}
		List<Integer> wayList = StringUtil.splitStr2IntegerList(msg.getString("ways"),",");

		try {
			worldTroop.lockTroop();
			WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
			//状态不对，无法移动
			if(!TroopState.isCanMove(worldTroop.getState())) {
				return;
			}
			if(wayList.get(0) != worldTroop.getCityId()) {
				wayList.add(0,worldTroop.getCityId());
			}
			//检查是否能把通过这些点
			if(!troopBiz.canAdoptWays(player,worldTroop,wayList)){
				player.sendErrorMsg(SysConstant.Move_Ways_LockCity);
				return;
			}
			//当前部队所在城市
			//从此城市删除此部队
			if(worldCity != null && !worldCity.removeTroopAndSave(troopId)) {
				log.error(troopId+"删除失败:在"+worldCity.getId()+" 删除失败!");
			}
			troopBiz.stratMove(worldTroop, wayList);
			troopBiz.sendWorldTroopUpdate(player, worldTroop);
			//广播城市部队变化
			if(worldCity != null) {
				worldBiz.broadWorldCityTroopChange(worldCity);
			}

		} finally {
			worldTroop.unlockTroop();
		}

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_DispatchWorldTroop);
		retMsg.addProperty("troopId",troopId);
		player.sendMsg(retMsg);
	}
	
	@MsgMethod(MessageComm.C2S_AirborneTroop)
	public void airborneTroop(Player player,JsonMsg msg) {
		if(!GuildWarUtils.isOpenWar(player.getServerId())) {
			player.sendErrorMsg(SysConstant.WarTimeNot);
			return;
		}
		String troopId = msg.getString("troopId");
		int targetCityId = msg.getInt("cityId");
		//路径 A->B->C
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		try {
			worldTroop.lockTroop();
			if(worldTroop == null || !troopBiz.isUnlockCity(player, targetCityId)) {
				return;//部队不存在
			}
			//判断是否可以操作移动
			if(!troopBiz.worldTroopIsCanEditor(player, worldTroop)) {
				player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
				return;
			}
			//判断是否可以空投
			if(WorldType.getTypeByCityId(targetCityId).getType() != worldTroop.getWorldType()) {
				player.sendErrorMsg(SysConstant.WorldTroop_Airborne_NotSameWorld);
				return;//部队不存在
			}
			if(!worldTroopBiz.isCanOperationTroop(worldTroop)){
				player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
				return;
			}
			WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(targetCityId);
			if(worldCity.getCityStatus().haveCityStatus(CityStatusType.NoAirdrop)) {
				player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
				return;
			}
			Guild guild = guildBiz.getGuild(player);
			if(guild == null) {
				return;
			}
			AirdropTatics airdropTatics = (AirdropTatics)guild.getGuildTactics().getGuildTactics(GuildTacticsType.Airdrop,targetCityId);
			if(airdropTatics == null || airdropTatics.getTimes() <= 0) {
				player.sendErrorMsg(SysConstant.Guild_Tec_AirborneNoTimes);
				return;
			}
			airdropTatics.reduceTimes();//减少次数
			if(airdropTatics.getTimes() <= 0) {
				guild.getGuildTactics().removeGuildTactics(airdropTatics);
			}
			guild.getGuildTactics().SetChanged();
			guild.saveDB();
			guildTechBiz.boradGuildTacticsUpdate(guild);//广播次数变化
			airTroop(player,worldTroop,targetCityId);
		} finally {
			worldTroop.unlockTroop();
		}
	}
	
	
	//停止移动
	@MsgMethod(MessageComm.C2S_Troop_StropMove)
	public void stopMove(Player player,JsonMsg msg){
		String troopId = msg.getString("troopId");
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null) {
			return;
		}
		try {
			worldTroop.lockTroop();
			
			if(worldTroop.getState()!=TroopState.Move.getType()){
				return;
			}
			troopBiz.stopMove(player,worldTroop);
			if(CollUtil.isEmpty(worldTroop.getTroopWay().getWayList())) {
				return;
			}
			JsonMsg result = new JsonMsg(MessageComm.S2C_Troop_StropMove);
			result.addProperty("nextCityId", worldTroop.getTroopWay().getWayList().getLast());
			result.addProperty("troopId", troopId);
			player.sendMsg(result);
			
		} finally {
			worldTroop.unlockTroop();
		}
	}
	
	//改变路线
	@MsgMethod(MessageComm.C2S_Troop_ChangeWay)
	public void changeWay(Player player,JsonMsg msg){
		String troopId = msg.getString("troopId");
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null) {
			return;
		}
		if(!worldTroopBiz.isCanOperationTroop(worldTroop)){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}
		//路径 A->B->C
		List<Integer> wayList = StringUtil.splitStr2IntegerList(msg.getString("ways"), ",");
		if(wayList.size() != Sets.newHashSet(wayList).size()) {
			log.error("派出部队出错:"+GSONUtils.ToJSONString(wayList));
			return;
		}
		try {
			worldTroop.lockTroop();
			
			List<Integer> oldWay = worldTroop.getTroopWay().getWayList();
			if(CollUtil.isEmpty(oldWay)) {
				return;
			}
			if(worldTroop.getState()==TroopState.None.getType()){
				wayList.remove(0);
				msg.addProperty("ways", StringUtil.list2Str(wayList, ","));
				dispatchWorldTroop(player, msg);
				return;
			}
			int lastTarget =  oldWay.get(0);//上个路线的下一个目标点加进去(比如从B到C到D,此时从B到C，路线为C,D)
			if(lastTarget!=wayList.get(0)){
				wayList.add(0, lastTarget);
			}
			//判断是否可以改变路线
			WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(lastTarget);
			if(troopBiz.isEnterCityFightForMove(player,worldTroop, worldCity)) {
				return;//下一个城池是敌方 无法改变路线
			}
			if(!troopBiz.canAdoptWays(player, worldTroop, wayList)){
				return;
			}
			worldTroop.getTroopWay().setWayList(Lists.newLinkedList(wayList));
			//worldTroop.getTroopWay().setStartCityId(lastTarget);
			worldTroop.saveDB();
			troopBiz.sendWorldTroopUpdate(player, worldTroop);
			
		} finally {
			worldTroop.unlockTroop();
		}
	}
	//突进
	@MsgMethod(MessageComm.C2S_AdvanceWorldTroop)
	public void advance(Player player,JsonMsg msg){
		String troopId = msg.getString("troopId");
		if(!worldTroopBiz.isCanOperationTroop(player.getServerId(),troopId)){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}		
		int cityId = msg.getInt("cityId");//目的城市
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
		if(!troopBiz.isCanAdvance(player, worldTroop, cityId)){
			player.sendMsg(MessageComm.S2C_AdvanceWorldTroop, troopId);
			return;
		}
		//突进到城市
		troopBiz.advanceToCity(player, worldTroop, worldCity, cityId);

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_AdvanceWorldTroop);
		retMsg.addProperty("troopId",troopId);
		player.sendMsg(retMsg);
	}

	//获取可以突进到此城池的部队列表
	@MsgMethod(MessageComm.C2S_AdvanceCityTroop)
	public void C2S_AdvanceCityTroop(Player player,JsonMsg msg){
		int cityId = msg.getInt("cityId");//目的城市
		List<Integer> linkList = cityConfig.getCityById(cityId).getLinkCityIds();

		Map<String,Integer> troopMap = Maps.newHashMap();
		for (WorldTroop value : TroopServerContainer.of(player).getPlayerTroopMap().values()) {
			if(!linkList.contains(value.getCityId()) || value.getCityId() == cityId) {
				continue;
			}
			if(!worldTroopBiz.isCanOperationTroop(value)) {
				continue;
			}
			if(!troopBiz.isCanAdvance(player, value, cityId,false)){
				continue;
			}
			troopMap.put(value.getId(),value.getCityId());
		}
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_AdvanceCityTroop);
		retMsg.addProperty("troopMap",troopMap);
		player.sendMsg(retMsg);
	}

	//撤退
	@MsgMethod(MessageComm.C2S_RetreatWorldTroop)
	public void retreat(Player player,JsonMsg msg){
		String troopId = msg.getString("troopId");
		int cityId = msg.getInt("cityId");//目的城市
		if(!worldTroopBiz.isCanOperationTroop(player.getServerId(),troopId)){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
//		if(worldCity.getCityStatus().haveCityStatus(CityStatusType.Roadblock)) {
//			player.sendErrorMsg(SysConstant.WorldTroop_Roadblock);
//			player.sendMsg(MessageComm.S2C_RetreatWorldTroop, troopId);
//			return;
//		}
		if(!troopBiz.isCanRetreat(player, worldTroop, cityId)){
			JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_RetreatWorldTroop);
			retMsg.addProperty("troopId",troopId);
			player.sendMsg(retMsg);
			return;
		}
		if(!worldCity.removeTroop(troopId)) {
			return;
		}
		//广播城市部队变化
		worldBiz.broadWorldCityTroopChange(worldCity);
		
		List<Integer> wayList = Lists.newArrayList(cityId);
		//加入起点
		wayList.add(0, worldCity.getId());
		worldCity.saveDB();
		//开始行进
		troopBiz.stratMove(worldTroop,wayList);
		//增加血量
		worldTroopBiz.addWorldTroopHpForRetreat(player, worldTroop);

		troopBiz.sendWorldTroopUpdate(player, worldTroop);

		player.notifyObservers(ObservableEnum.TroopRetreat);
		player.sendUserUpdateMsg();

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_RetreatWorldTroop);
		retMsg.addProperty("troopId",troopId);
		player.sendMsg(retMsg);
	}
	
	@MsgMethod(MessageComm.C2S_Troop_PvpOneByOne)
	public void pvpOneByOne(Player player,JsonMsg msg){
		int cityId = player.playerTemp().getCurCityId();//目的城市
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		if(worldCity == null) {
			return;
		}
		if(!player.playerTemp().isCanPvp()) {
			return;
		}
		try {
			worldCity.lockWrite();
			if(!worldCity.hasFight()) {
				return;
			}
			List<String> canPvpList = TroopServerContainer.of(player).getWorldTroopByPlayer(player).stream()
					.filter(e -> e.getCityId() == worldCity.getId())
					.filter(e -> e.getState() == TroopState.FightWait.getType())
					.map(e -> e.getId())
					.collect(Collectors.toList());
			if(canPvpList.isEmpty()) {
				player.sendErrorMsg(SysConstant.Troop_Pvp1v1_NotSelfTroop);
				return;//没有可单挑部队
			}
			if(!guildCityFightBiz.isFriendCity(player, worldCity)) {
				//从攻击方查找
				int type = troopFightBiz.doPvp1v1(worldCity, player, AtkDefType.ATK, canPvpList);
				if(type > 0) {
					if(type == 1) return;
					return;
				}
			}else{//从防守方查找
				int type = troopFightBiz.doPvp1v1(worldCity, player, AtkDefType.DEF, canPvpList);
				if(type > 0) {
					if(type == 1) return;
					return;
				}
			}
			player.sendErrorMsg(SysConstant.Troop_Pvp1v1_NotTargeTroop);
		} finally{
			worldCity.unlockWrite();
		}
	}
	
	@MsgMethod(MessageComm.C2S_HandTroopFullHp)
	public void handTroopFullHp(Player player,JsonMsg msg){
		String troopId = msg.getString("troopId");
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		//判断俘虏
		List<Integer> beCaptiveTankIds = player.playerCaptive().getBeCaptiveTank();
		//死亡或者可回血状态
		List<TankArmy> recoverList = worldTroop.getTroopArmy().getTankList().stream()
								.filter(e -> !e.isFullHp())
								.filter(e -> !beCaptiveTankIds.contains(e.getId()))
								.collect(Collectors.toList());
		boolean isCanRecoverHp = worldTroop.getState() == TroopState.Death.getType()
				|| worldTroop.getState() == TroopState.None.getType()
				 && CollUtil.isNotEmpty(recoverList);
		if(!isCanRecoverHp) {
			return;
		}

		int oldState = worldTroop.getState();
		//需要总秒数
		long totalSecond = troopBiz.calTroopRecoveTime(player,worldTroop,recoverList);
		//减去已经恢复的秒数
		totalSecond -= worldTroop.getTroopArmy().getCurRecoverSecond();

		FullTroopResult result = worldTroopBiz.checkSpendHandFullTroopOrKF(player,totalSecond);
		//扣除满血所需金币
		if(!result.isSuc()) {
			player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}

		worldTroop.getTroopArmy().changeTroopTankFullHp();
		worldTroop.changeState(TroopState.None);//改为空闲状态
		if(oldState == TroopState.Death.getType()) {
			//把部队放到该放的地方
			troopBiz.addWorldTroopToWorldAndUpdate(player, worldTroop);
			player.notifyObservers(ObservableEnum.TroopRevive, worldTroop,result.getSpendGold());
		}else{
			worldTroop.saveDB();
			troopBiz.sendWorldTroopUpdate(player, worldTroop);
		}
		player.notifyObservers(ObservableEnum.TroopRepairNow, worldTroop,result.getSpendGold());
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_HandTroopFullHp);
		serverMsg.addProperty("spendGold", result.getSpendGold());
		serverMsg.addProperty("spendItemNum", result.getSpendItemNum());
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_CloneTroop)
	public void cloneTroop(Player player,JsonMsg msg){
		if(player.playerCommander().getMilitaryLv() < commValueConfig.getCommValue(CommonValueType.CloneTroopLv)) {
			player.sendErrorMsg(SysConstant.Player_Level_Not_Enough);
			return;//等级不足
		}
		String troopId = msg.getString("troopId");
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null || worldTroop.getState() == TroopState.Death.getType()) {
			player.sendErrorMsg(SysConstant.WorldTroop_TroopDeath_Clone);
			return;
		}
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
		if(worldCity == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_CityNot_Clone);
			return;
		}
		if(player.isKFPlayer()) {
			doKFPlayerCloneTroop(player,msg,worldCity,worldTroop);
		}else{
			doCloneTroop(player,msg,worldCity,worldTroop);
		}
	}

	public void doCloneTroop(Player player,JsonMsg msg,WorldCity worldCity,WorldTroop worldTroop) {
		boolean isNormalWorld = worldCity.getWorldType() == WorldType.Normal;
		if(isNormalWorld) {
			// 今天购买次数
			long todayCount = player.getPlayerStatistics().getTodayStatistics(StatisticsType.CloneTroopCreate);
			// 是否达还有次数
			int maxCount = commValueConfig.getCommValue(CommonValueType.CloneTroopMaxCount);
			if(todayCount >= maxCount) {
				player.sendErrorMsg(SysConstant.VIP_LV_NOT_ENOUGH);
				return;//等级不足
			}
		}
		//检查道具
		Items spendItem = troopBiz.calPlayerCloneItem(player);
		if(!itemBiz.checkItemEnough(player, spendItem)) {
			player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}
		boolean isSuc = troopFightBiz.clonePlayerTroop(player, worldTroop.getId());
		if(!isSuc) {
			return;
		}
		itemBiz.reduceItem(player, spendItem, LogType.CloneTroop);
		if(isNormalWorld) {
			player.getPlayerStatistics().addTodayStatistics(StatisticsType.CloneTroopCreate);
		}
		//发出事件
		player.notifyObservers(ObservableEnum.CloneTroop);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_CloneTroop);
	}

	public void doKFPlayerCloneTroop(Player player,JsonMsg msg,WorldCity worldCity,WorldTroop worldTroop) {
		IGameRpc gameRpc = player.playerTemp().getKfpServerUrl().getGameRpc();
		String spendItems = gameRpc.calCloneTroopSpend(player.getId());
		if(StrUtil.isEmpty(spendItems)) {
			return;
		}
		boolean isSuc = troopFightBiz.clonePlayerTroop(player, worldTroop.getId());
		if(!isSuc) {
			return;
		}
		gameRpc.addOrSpendPlayerItem(player.getId(),spendItems,false,"kf_clone_troop");
	}

	
	@MsgMethod(MessageComm.C2S_RewardCloneTroopExp)
	public void rewardCloneTroopExp(Player player,JsonMsg msg){
		long exp = player.playerCloneTroops().rewardExp();
		//转换成道具
		Items items = new Items(ItemConstant.CloneExpItem,exp,ItemType.ITEM);

		itemBiz.addItem(player,items,LogType.CloneTroop);
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_RewardCloneTroopExp);
		serverMsg.addProperty("exp", exp);
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_Troop_BindFormation)
	public void bindFormation(Player player,JsonMsg msg){
		String troopId = msg.getString("troopId");
		int id = msg.getInt("id");//飞行编队的id
		if(id<0||id>5) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		if(!worldTroopBiz.isCanOperationTroop(player.getServerId(),troopId)){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}
		if(!player.playerTroops().isContain(troopId)) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorNot);
			return;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_EditorNot);
			return;
		}
		worldTroop.getTroopInfo().bindFormation(id);
		worldTroop.saveDB();
		player.sendUserUpdateMsg();
		troopBiz.sendWorldTroopUpdate(player, worldTroop);
		player.sendMsg(MessageComm.S2C_Troop_BindFormation);
	}
	
	public void airTroop(Player player,WorldTroop worldTroop,int targetCityId) {
		//部队从别的城池删除
		String troopId = worldTroop.getId();
		WorldCity oldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
		if(oldCity != null) {
			if(!oldCity.removeTroopAndSave(worldTroop.getId())) {
				log.error(troopId+"删除失败:空降"+oldCity.getId()+" 删除失败!");
			}
		}
		//直接空降到某城市
		worldBiz.troopEnterCity(player, worldTroop, targetCityId);
		troopBiz.sendWorldTroopUpdate(player, worldTroop);
		//广播空降消息
		troopBiz.sendTroopMoveMsg(player,worldTroop,0,targetCityId,1,true);
	}
}
