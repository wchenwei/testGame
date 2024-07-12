package com.hm.action.commander;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.commander.biz.AircraftCarrierBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.AircraftCarrierConfig;
import com.hm.config.excel.templaextra.CvDrawConfigTemplateImpl;
import com.hm.config.excel.templaextra.CvEngineLevelTemplateImpl;
import com.hm.config.excel.templaextra.CvIslandTemplateImpl;
import com.hm.config.excel.templaextra.CvLevelTemplateImpl;
import com.hm.enums.ActionType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Aircraft;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerAircraftCarrier;
import com.hm.observer.ObservableEnum;
import com.hm.redis.Formation;
import com.hm.redis.PlayerAirFormation;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description: 航母
 * @author xjt  
 * @date 2020年12月11日10:49:39
 * @version V1.0
 */
@Action
public class AircraftCarrierAction extends AbstractPlayerAction{
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private AircraftCarrierBiz aircraftCarrierBiz;
	@Resource
	private AircraftCarrierConfig aircraftCarrierConfig;
	@Resource
	private LogBiz logBiz;
	
	/**
	 * 航母升级
	 */
	@MsgMethod(MessageComm.C2S_Aircraft_Carrier_LvUp)
	public void lvUp(Player player, JsonMsg msg){
		if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.AircraftCarrier)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		PlayerAircraftCarrier playerAircraftCarrier = player.playerAircraftCarrier();
		int lv = playerAircraftCarrier.getLv();
		int maxLv = aircraftCarrierConfig.getMaxLv();
		if (lv >= maxLv){
			player.sendErrorMsg(SysConstant.LV_MAX);
			return;
		}
		CvLevelTemplateImpl cvLevelTemplate = aircraftCarrierConfig.getCvLevelTemplate(lv);
		if (cvLevelTemplate == null){
			return;
		}
		List<Items> costItems = cvLevelTemplate.getCostItems();
		if (!itemBiz.checkItemEnoughAndSpend(player,costItems,LogType.AircraftCarrierLvUp.value(lv))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		playerAircraftCarrier.upLv();
		player.notifyObservers(ObservableEnum.AircraftCarrierLvUp);
		player.notifyObservers(ObservableEnum.AircraftCarrierLvUpEvent, lv, playerAircraftCarrier.getLv(), costItems);
		logBiz.addPlayerActionLog(player, ActionType.AircraftCarrierStarUp.getCode(),String.valueOf(playerAircraftCarrier.getLv()));
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Aircraft_Carrier_LvUp);
	}

	/**
	 * 航母修造
	 */
	@MsgMethod(MessageComm.C2S_Aircraft_Carrier_Build)
	public void build(Player player, JsonMsg msg){
		if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.AircraftCarrier)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		PlayerAircraftCarrier playerAircraftCarrier = player.playerAircraftCarrier();
		int engineLv = playerAircraftCarrier.getEngineLv();
		int maxEngineLv = aircraftCarrierConfig.getMaxEngineLv();
		if (engineLv >= maxEngineLv){
			player.sendErrorMsg(SysConstant.LV_MAX);
			return;
		}
		CvEngineLevelTemplateImpl cvEngineLevelTemplate = aircraftCarrierConfig.getCvEngineLevelTemplate(engineLv);
		if (cvEngineLevelTemplate == null){
			return;
		}
		if (playerAircraftCarrier.getLv() < cvEngineLevelTemplate.getUnlock_level()){
			player.sendErrorMsg(SysConstant.Aircraft_Carrier_Lv_Not_Enough);
			return;
		}
		List<Items> costItems = cvEngineLevelTemplate.getCostItems();
		if (!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.AircraftCarrierEngineLvUp.value(engineLv))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		playerAircraftCarrier.upEngineLv();
		player.notifyObservers(ObservableEnum.AircraftCarrierBuild, engineLv, playerAircraftCarrier.getEngineLv(), costItems);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Aircraft_Carrier_Build);
	}

	/**
	 * 飞机上阵
	 */
	@MsgMethod(MessageComm.C2S_Aircraft_Carrier_Edit)
	public void edit(Player player, JsonMsg msg){
		PlayerAircraftCarrier playerAircraftCarrier = player.playerAircraftCarrier();
		String troop = msg.getString("troop");// 上阵index:uid 下阵 index:""  互换 index1:uid1,index2:uid2
		if (StrUtil.isEmpty(troop)){
			return;
		}
		CvLevelTemplateImpl cvLevelTemplate = aircraftCarrierConfig.getCvLevelTemplate(playerAircraftCarrier.getLv());
		if (cvLevelTemplate == null){
			return;
		}
		// 航母位置检查
		if (!aircraftCarrierBiz.checkAircraftPosition(player, cvLevelTemplate, troop)){
			player.sendErrorMsg(SysConstant.Aircraft_Carrier_Lv_Not_Enough);
			return;
		}
		// 飞机类型检查
		if (!aircraftCarrierBiz.checkAirplaneTypeUnique(player,troop)){
			player.sendErrorMsg(SysConstant.Aircraft_Carrier_Airplane_type_repeat);
			return;
		}
		aircraftCarrierBiz.upAircraft2Carrier(player, troop);
		player.notifyObservers(ObservableEnum.AircraftCarrierEit);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Aircraft_Carrier_Edit);
	}

	
	@MsgMethod ( MessageComm.C2S_Aircraft_StarUp)
	public void starUp(Player player, JsonMsg msg){
		String uid = msg.getString("uid");
		List<String> uids = StringUtil.splitStr2StrList(msg.getString("uids"), ",");
		Aircraft aircraft = player.playerAircraft().getAircraft(uid);
		if(aircraft==null) {
			//沒有该飞机
			return;
		}
		List<Aircraft> aircrafts = player.playerAircraft().getAircraft(uids);
		//检查要消耗的飞机是否存在且为同种类型的飞机
		if(!aircraftCarrierBiz.isCanStarUp(player, aircraft.getId(), aircrafts)) {
			return;
		}
		int star = aircrafts.stream().mapToInt(t ->t.getStar()).sum();
		//删除飞机
		player.playerAircraft().removeAircrafts(aircrafts.stream().map(t ->t.getUid()).collect(Collectors.toList()));
		//给飞机升星
		int currentStar = aircraft.getStar();
		aircraft.starUp(star);
		player.playerAircraft().SetChanged();
		player.notifyObservers(ObservableEnum.AircraftStarUp, uid, player.playerAircraftCarrier().isAircraftUp(uid), currentStar, aircraft.getStar(), uids);
		logBiz.addPlayerActionLog(player, ActionType.AircraftStarUp.getCode(),String.format("%s_%s", aircraft.getId(), aircraft.getStar()));
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Aircraft_StarUp,uid);
	}
	//抽奖
	@MsgMethod ( MessageComm.C2S_Aircraft_LuckDraw)
	public void luckDraw(Player player, JsonMsg msg){
		int type = msg.getInt("type");
		int id = msg.getInt("id");
		int count = type==0?1:10;
		
		CvDrawConfigTemplateImpl template = aircraftCarrierConfig.getDrawTemplate(id);
		if(template==null) {
			return;
		}
		List<Items> costs = type==0?template.getOnceCosts():template.getTenCosts();
		if(!itemBiz.checkItemEnoughAndSpend(player, costs, LogType.AircraftLuckDraw)) {
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		List<Items> rewards = aircraftCarrierBiz.luckDraw(player,id,type,count);
		itemBiz.addItem(player, rewards, LogType.AircraftLuckDraw);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Aircraft_LuckDraw,rewards);
	}
	
	//分解
	@MsgMethod ( MessageComm.C2S_Aircraft_Decompose)
	public void decompose(Player player, JsonMsg msg){
		List<String> uids = StringUtil.splitStr2StrList(msg.getString("ids"), ",");
		List<Items> rewards = aircraftCarrierBiz.getDecomposeReward(player,uids);
		//分解
		List<Integer> ids = aircraftCarrierBiz.decompose(player, uids);
		itemBiz.addItem(player, rewards, LogType.AircraftLuckDraw.value(ids+""));
		player.notifyObservers(ObservableEnum.AircraftDecomposeEvent, uids, rewards);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Aircraft_Decompose,rewards);
	}
	
	//飞机编队
	@MsgMethod ( MessageComm.C2S_Aircraft_AirFormation)
	public void airFormation(Player player, JsonMsg msg){
		List<String> uids = StringUtil.splitStr2StrList(msg.getString("ids"), ",");
		int id = msg.getInt("id");//第几队
		//校验队伍
		if(!aircraftCarrierBiz.checkAirFormation(player,id,uids)) {
			player.sendErrorMsg(SysConstant.Aircraft_Formation_Not);
			return;
		};
		PlayerAirFormation formation = PlayerAirFormation.getOrCreate(player.getId());
		formation.addFormation(id,new Formation(uids));
		formation.updateRedis();
		
		aircraftCarrierBiz.sendPlayerAirFormation(player);
		player.sendMsg(MessageComm.S2C_Aircraft_AirFormation,id);
	}
	
	//获取飞机编队
	@MsgMethod ( MessageComm.C2S_Aircraft_GetAirFormation)
	public void getFormations(Player player, JsonMsg msg){
		aircraftCarrierBiz.sendPlayerAirFormation(player);
	}
	
	//解散飞机编队
	@MsgMethod ( MessageComm.C2S_Aircraft_DisbandAirFormation)
	public void disbandAirFormation(Player player, JsonMsg msg){
		int id = msg.getInt("id");//第几队
		PlayerAirFormation formation = PlayerAirFormation.getOrCreate(player.getId());
		formation.disbandAirFromation(id);
		formation.updateRedis();
		aircraftCarrierBiz.sendPlayerAirFormation(player);
		player.sendMsg(MessageComm.S2C_Aircraft_DisbandAirFormation,id);
	}

	/**
	 * 舰岛，升级
	 * @param player
	 * @param msg
	 */
	@MsgMethod ( MessageComm.C2S_Aircraft_Island_Lvup)
	public void islandLvUp(Player player, JsonMsg msg){
		int type = msg.getInt("type");//舰岛类型
		PlayerAircraftCarrier playerAircraftCarrier = player.playerAircraftCarrier();
		int islandId = playerAircraftCarrier.getIsland(type);
		CvIslandTemplateImpl isLand = aircraftCarrierConfig.getIslandById(islandId);
		//用户未开启舰岛
		CvIslandTemplateImpl nextIsLand = Optional.ofNullable(isLand)
				.map(e->aircraftCarrierConfig.getNextLvIsland(e.getType(), e.getLevel()))
				.orElse(null);
		//已经满级,或者未开启
		if(null==nextIsLand) {
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		List<Items> costs = isLand.getCostItems();
		//扣资源升级
		if(!CollectionUtil.isEmpty(costs) && !itemBiz.checkItemEnoughAndSpend(player, costs, LogType.AircraftIslandLvup.value(nextIsLand.getType()))) {
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		playerAircraftCarrier.islandLvup(nextIsLand);
		player.notifyObservers(ObservableEnum.AircraftIslandLvup, type, islandId, nextIsLand.getId(), costs);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Aircraft_Island_Lvup,nextIsLand.getId());
	}
	
}
