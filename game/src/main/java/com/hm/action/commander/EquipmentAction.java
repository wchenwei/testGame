package com.hm.action.commander;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.commander.biz.EquipmentBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankAttrBiz;
import com.hm.config.EquipmentConfig;
import com.hm.config.excel.templaextra.PlayerArmCircleExtraTemplate;
import com.hm.config.excel.templaextra.PlayerArmExtraTemplate;
import com.hm.config.excel.templaextra.PlayerArmStoneTemplate;
import com.hm.config.excel.templaextra.PlayerArmStrengthenTemplate;
import com.hm.enums.EquCircleType;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Equipment;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Action
public class EquipmentAction extends AbstractPlayerAction{
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private EquipmentBiz equipmentBiz;
	@Resource
	private EquipmentConfig equipmentConfig;

	@MsgMethod(MessageComm.C2S_Equ_QuaLvUp)
	public void composeEqu(Player player,JsonMsg msg){
		int id = msg.getInt("id");
		Equipment equipment = player.playerEquip().getEquipment(id);
		//找出下一级升级的装备
		PlayerArmExtraTemplate nextTemplate = equipmentConfig.getNextUpEquip(id,equipment.getEquId());
		Items spendItem = ItemType.ITEM.createItems(nextTemplate.getPiece_id(),nextTemplate.getPiece_count());

		if(!itemBiz.checkItemEnoughAndSpend(player, spendItem, LogType.ComposeEqu.value(nextTemplate.getId()))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		Map<TankAttrType, Double> oldMap = equipmentConfig.getEquAttr(equipment.getEquId());
		player.playerEquip().changeEqu(id, nextTemplate);
		player.notifyObservers(ObservableEnum.ComposeEqu, nextTemplate.getId());

		player.sendUserUpdateMsg();

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Equ_QuaLvUp);
		retMsg.addProperty("changeAttr", TankAttrBiz.diffAttrMap(oldMap,nextTemplate.getAttrMap()));
		player.sendMsg(retMsg);
	}



	/**
	 * 强化装备
	 * @param player
	 * @param msg
	 */
	@MsgMethod(MessageComm.C2S_Equ_Strengthen)
	public void strengthen(Player player, JsonMsg msg){
		int id = msg.getInt("id");//部位id

		Equipment equipment = player.playerEquip().getEquipment(id);
		int lv = equipment.getStrengthenLv();
		int errorCode = eqIsCanLvUp(player,equipment);
		if(errorCode > 0){
			player.sendErrorMsg(errorCode);
			return;
		}
		PlayerArmStrengthenTemplate template = equipmentConfig.getStrengthen(lv+1);
		List<Items> cost = template.getCost();

		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.StrengthenEqu.value(id))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		//记录老数据
		Map<TankAttrType,Double> oldMap = equipmentConfig.getStrengthenAttr(equipment.getId(),equipment.getStrengthenLv());
		PlayerArmCircleExtraTemplate oldCircleTemplate = equipmentConfig.getCircle(player,EquCircleType.StrengthenCircle);
		//强化
		equipment.strengthen(1);

		player.playerEquip().SetChanged();
		player.notifyObservers(ObservableEnum.CHEquStreng, id, lv, equipment.getStrengthenLv(), cost);


		//计算强化大师
		boolean isCircleLvUp = equipmentBiz.calEquipCircle(player, EquCircleType.StrengthenCircle);

		//发出强化装备信号，信号会引起坦克属性和战力的变化
		player.notifyObservers(ObservableEnum.StrengthenEqu, id);
		player.sendUserUpdateMsg();

		//属性提升
		Map<Integer,Double> diffMap = TankAttrBiz.diffAttrMap(oldMap,equipmentConfig.getStrengthenAttr(equipment.getId(),equipment.getStrengthenLv()));
//		if(isCircleLvUp) {
//			PlayerArmCircleExtraTemplate newCircleTemplate = equipmentConfig.getCircle(player,EquCircleType.StrengthenCircle);
//		}
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Equ_Strengthen);
		retMsg.addProperty("changeAttr", diffMap);
		player.sendMsg(retMsg);
	}

	//一键强化
	@MsgMethod(MessageComm.C2S_Equ_Strengthen_OneKey)
	public void C2S_Equ_Strengthen_OneKey(Player player, JsonMsg msg){
		boolean isLvUp = false;
		Equipment luck = findCanLvUpEq(player);
		if(luck == null) {
			player.sendErrorMsg(SysConstant.OneKeyEqStrenNoAny);
			return;
		}
		Set<Integer> upIdList = Sets.newHashSet();
		Map<TankAttrType,Double> oldMap = equipmentConfig.getAllEquStrengthenAttrMap(player);
		for (int i = 0; i < 1000; i++) {
			if(luck == null) {
				break;
			}
			int lv = luck.getStrengthenLv();
			PlayerArmStrengthenTemplate template = equipmentConfig.getStrengthen(lv+1);
			List<Items> cost = template.getCost();

			if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.StrengthenEqu.value(luck.getId()))){
				break;
			}
			upIdList.add(luck.getId());
			//强化
			luck.strengthen(1);
			player.playerEquip().SetChanged();
			isLvUp = true;
			//下一个
			luck = findCanLvUpEq(player);
		}


		if(isLvUp) {
			//计算强化大师
			equipmentBiz.calEquipCircle(player, EquCircleType.StrengthenCircle);

			player.notifyObservers(ObservableEnum.StrengthenEqu);
			player.sendUserUpdateMsg();
		}
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Equ_Strengthen_OneKey);
		if(isLvUp) {
			retMsg.addProperty("upIdList",upIdList);
			retMsg.addProperty("changeAttr",TankAttrBiz.diffAttrMap(oldMap,equipmentConfig.getAllEquStrengthenAttrMap(player)));
		}
		player.sendMsg(retMsg);
	}

	public Equipment findCanLvUpEq(Player player) {
		//按照等级从高到低排序
		List<Equipment> sortList = Arrays.stream(player.playerEquip().getEqus())
				.filter(e -> e.isUnlock())
				.filter(e -> eqIsCanLvUp(player,e) <= 0)
				.sorted(Comparator.comparingInt(Equipment::getStrengthenLv))
				.collect(Collectors.toList());

		if(CollUtil.isEmpty(sortList)) {
			return null;//没有可以升级的
		}
		return sortList.get(0);
	}


	//判断装备是否可以强化
	public int eqIsCanLvUp(Player player,Equipment equipment) {
		int lv = equipment.getStrengthenLv();
		int maxLv = equipmentConfig.getMaxStrengthenLv();
		if(lv >= maxLv){
			return SysConstant.OneKeyEqStrenNoAny;
		}
		int eqQua = equipmentConfig.getEquTemplate(equipment.getEquId()).getQuality();
		PlayerArmStrengthenTemplate template = equipmentConfig.getStrengthen(lv+1);
		if(eqQua < template.getQuality()) {
			return SysConstant.OneKeyEqStrenNoAny;
		}
		if(!itemBiz.checkItemEnough(player, template.getCost())){
			return SysConstant.ITEMS_NOT_ENOUGH;
		}
		return 0;
	}

	//一键升级宝石
	@MsgMethod(MessageComm.C2S_Stone_LvUp_OneKey)
	public void C2S_Stone_LvUp_OneKey(Player player, JsonMsg msg){
		Map<TankAttrType,Double> oldMap = equipmentConfig.getAllEquStoneAttrMap(player);

		Set<String> changeList = Sets.newHashSet();
		for (Equipment equs : player.playerEquip().getEqus()) {
			//对每个位置的宝石进行升级
			doStoneOneLvUpForCount(player,equs,changeList);
		}
		boolean isChange = player.playerEquip().Changed();
		if(!isChange) {
			if(equipmentBiz.allStoneIsMaxLv(player)) {
				player.sendErrorMsg(SysConstant.OneKeyEqStoneAllMaxLv);
			}else{
				player.sendErrorMsg(SysConstant.OneKeyEqStoneNotFind);
				//宝石道具不足
				player.notifyObservers(ObservableEnum.ItemNotEnough,ItemType.STONE.getType(),0);
			}
			return;
		}
		//计算宝石大师
		equipmentBiz.calEquipCircle(player, EquCircleType.StoneCircle);

		if (!changeList.isEmpty()) {
			player.notifyObservers(ObservableEnum.StoneLvUp, changeList.size());
		}
		player.notifyObservers(ObservableEnum.ChangeStone);
		player.sendUserUpdateMsg();

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Stone_LvUp_OneKey);
		retMsg.addProperty("changeList",changeList);
		retMsg.addProperty("changeAttr",TankAttrBiz.diffAttrMap(oldMap,equipmentConfig.getAllEquStoneAttrMap(player)));
		player.sendMsg(retMsg);
	}

	//一键镶嵌宝石
	@MsgMethod(MessageComm.C2S_Stone_Install_OneKey)
	public void C2S_Stone_Install_OneKey(Player player, JsonMsg msg){
		Map<TankAttrType,Double> oldMap = equipmentConfig.getAllEquStoneAttrMap(player);
		Set<String> changeList = Sets.newHashSet();

		for (Equipment equs : player.playerEquip().getEqus()) {
			if(equs.getEquId() <= 0) {
				continue;
			}
			changeEquStone(player,equs,changeList);
		}

		if(CollUtil.isEmpty(changeList)) {
			player.sendErrorMsg(SysConstant.OneKeyEqStoneNotUp);
			return;
		}
		//计算宝石大师
		equipmentBiz.calEquipCircle(player, EquCircleType.StoneCircle);

		player.notifyObservers(ObservableEnum.ChangeStone);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Stone_Install_OneKey);

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Stone_Install_OneKey);
		retMsg.addProperty("changeList",changeList);
		retMsg.addProperty("changeAttr",TankAttrBiz.diffAttrMap(oldMap,equipmentConfig.getAllEquStoneAttrMap(player)));
		player.sendMsg(retMsg);
	}

	/**
	 * 更换石头
	 */
	@MsgMethod(MessageComm.C2S_Stone_Change)
	public void changeStone(Player player, JsonMsg msg){
		int id = msg.getInt("id");//部位id
		int index = msg.getInt("index");//石头孔位
		int stoneId = msg.getInt("stoneId");//更换的石头id（id==0表示卸下）
		Equipment equ = player.playerEquip().getEquipment(id);
		if(equ.getStone()[index]==stoneId){//如果要更换的石头和原装备相同则直接返回结果
			player.sendMsg(MessageComm.S2C_Stone_Change);
			return;
		}
		if(stoneId > 0) {
			//该石头能否装备在该位置上(id==0为卸下该位置上的宝石，不需要检查部位条件)
			if(!equipmentConfig.checkStoneBody(stoneId, id)){
				return;
			}
			//没有该石头
			if(!itemBiz.checkItemEnoughAndSpend(player, new Items(stoneId,1,ItemType.STONE),LogType.ChangeStone)){
				player.sendErrorMsg(SysConstant.ITEM_HAVENOT);
				return;
			}
		}


		player.notifyObservers(ObservableEnum.CHEquChangeStone, id, index, equ.getEquId(), stoneId);

		equipmentBiz.changeStone(player,id,stoneId,index);
		//发出更换石头信号，信号会引起坦克属性和战力的变化
		player.notifyObservers(ObservableEnum.ChangeStone, id);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Stone_Change);
	}


	public boolean changeEquStone(Player player,Equipment equs,Set<String> changeList) {
		//找出此位置上比安装的宝石大的宝石列表
		List<Integer> idList = equipmentConfig.getPosSortStoneIdList(equs.getId());

		for (int i = idList.size()-1; i >= 0; i--) {
			int luckId = idList.get(i);
			long count = player.playerStone().getItemCount(luckId);
			if(count <= 0) {
				continue;
			}
			for (int j = 0; j < count; j++) {
				if(!changeStone(player,equs,luckId,changeList)) {
					return true;//找不到比我更小的宝石了 直接返回
				}
			}
		}
		return false;
	}

	public boolean changeStone(Player player,Equipment equs,int luckId,Set<String> changeList) {
		int minIndex = equs.findMineStoneIndex();
		int minStoneId = equs.getStone()[minIndex];
		if(minStoneId >= luckId) {
			return false;
		}
		//消耗
		itemBiz.reduceItem(player, new Items(luckId,1,ItemType.STONE),LogType.ChangeStone);
		//替换装备的宝石
		equs.changeStone(luckId,minIndex);
		if(minStoneId > 0){
			itemBiz.addItem(player, new Items(minStoneId, 1, ItemType.STONE),LogType.ChangeStone);
		}
		changeList.add(equs.getId()+"_"+minIndex);
		player.playerEquip().SetChanged();

		return true;
	}

	public void doStoneOneLvUpForCount(Player player,Equipment equs,Set<String> changeList) {
		for (int i = 0; i < 100; i++) {
			if(!doStoneOneLvUp(player,equs,changeList)) {
				return;
			}
		}
	}

	public boolean doStoneOneLvUp(Player player,Equipment equs,Set<String> changeList) {
		if(equs.getEquId() <= 0) {
			return false;
		}
		List<Integer> sortList = equs.getSortStoneIds();
		for (int stoneId : sortList) {
			PlayerArmStoneTemplate template = equipmentConfig.getStone(stoneId);
			if(template.getNext_id() <= 0) {
				continue;//都满级了
			}
			List<Integer> stoneList = equipmentConfig.getPosSortStoneIdList(equs.getId());
			List<Items> itemList = Lists.newArrayList();
			for (int tempId : stoneList) {
				if(tempId <= stoneId) {
					long count = player.playerStone().getItemCount(tempId);
					if(count > 0) {
						itemList.add(new Items(tempId,count,equipmentConfig.getStone(tempId).getLv1Stone()));
					}
				}
			}
			if(CollUtil.isEmpty(itemList)) {
				continue;//没有材料升级
			}
			int spend = template.getLv1Stone()*(template.getNext_need_count()-1);
			Map<Integer,Integer> spendItemMap = calStoneSpend(itemList,spend);
			if(CollUtil.isEmpty(spendItemMap)) {
				continue;//材料不够
			}
			int index = ArrayUtil.indexOf(equs.getStone(),stoneId);
			for (Map.Entry<Integer, Integer> entry : spendItemMap.entrySet()) {
				itemBiz.reduceItem(player,new Items(entry.getKey(),entry.getValue(),ItemType.STONE),LogType.LevleUpStone);
			}
			//替换玩家的宝石
			equs.changeStone(template.getNext_id(),index);
			changeList.add(equs.getId()+"_"+index);
			player.playerEquip().SetChanged();
			return true;
		}
		return false;
	}


	public static Map<Integer,Integer> calStoneSpend(List<Items> itemList, int target){
		Map<Integer,Integer> countMap = Maps.newHashMap();
		for (int i=itemList.size()-1; i>=0;i--){
			Items items = itemList.get(i);
			for (int j = 0; j < items.getCount() && target>=items.getType(); j++) {
				target -= items.getType();
				int id = items.getId();
				countMap.put(id,countMap.getOrDefault(id,0)+1);
			}
		}
		return target==0?countMap:null;
	}

}
