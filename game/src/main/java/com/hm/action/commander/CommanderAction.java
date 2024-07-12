 /**  
 * Project Name:SLG_GameFuture.
 * File Name:MailAction.java  
 * Package Name:com.hm.action.mail  
 * Date:2017年12月18日下午3:50:23  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */  
  
package com.hm.action.commander;

 import com.hm.action.AbstractPlayerAction;
 import com.hm.action.commander.biz.CommanderBiz;
 import com.hm.action.item.ItemBiz;
 import com.hm.action.tank.biz.TankAttrBiz;
 import com.hm.config.PlayerFunctionConfig;
 import com.hm.config.excel.CommValueConfig;
 import com.hm.config.excel.CommanderConfig;
 import com.hm.config.excel.TankConfig;
 import com.hm.config.excel.templaextra.SuperWeaponExTemplate;
 import com.hm.config.excel.templaextra.SuperWeaponUpgradeExtrTemplate;
 import com.hm.enums.CommonValueType;
 import com.hm.enums.LogType;
 import com.hm.enums.PlayerFunctionType;
 import com.hm.enums.TankAttrType;
 import com.hm.libcore.annotation.Action;
 import com.hm.libcore.annotation.MsgMethod;
 import com.hm.libcore.msg.JsonMsg;
 import com.hm.libcore.util.string.StringUtil;
 import com.hm.message.MessageComm;
 import com.hm.model.item.Items;
 import com.hm.model.player.Player;
 import com.hm.observer.ObservableEnum;
 import com.hm.sysConstant.SysConstant;
 import org.apache.commons.lang.StringUtils;

 import javax.annotation.Resource;
 import java.util.List;
 import java.util.Map;

 /**
 * ClassName: CommanderAction. <br/>  
 * date: 2017年12月18日 下午3:50:23 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Action
public class CommanderAction extends AbstractPlayerAction {
	@Resource
	private CommanderBiz commanderBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CommanderConfig commanderConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private PlayerFunctionConfig functionConfig;
	 @Resource
	 private TankAttrBiz tankAttrBiz;
	/**
	 * 座驾升级
	 *
	 * @author yanpeng 
	 * @param player
	 * @param msg  
	 *
	 */
	@MsgMethod (MessageComm.C2S_CarLvUp)
	public void carLvUp(Player player, JsonMsg msg){
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Mounts)){
			player.sendErrorMsg(SysConstant.Car_Lock);
			return; 
		}
		Map<TankAttrType, Double> oldMap = tankAttrBiz.calCarAttr(player);
		boolean isBuy = msg.getBoolean("isBuy");
		//自动强化，客户端循环调用该消息，直到没有资源或者玩家主动停止
		boolean result = commanderBiz.carlvUp(player,isBuy);
		if(result){
			player.sendUserUpdateMsg();
		}
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_CarLvUp);
		retMsg.addProperty("result",result);
		if(result) {
			retMsg.addProperty("changeAttr",TankAttrBiz.diffAttrMap(oldMap,tankAttrBiz.calCarAttr(player)));
		}
		player.sendMsg(retMsg);
	}
	

	
	/**
	 * 军衔升级
	 *
	 * @author yanpeng 
	 * @param player
	 * @param msg  
	 *
	 */
	@MsgMethod ( MessageComm.C2S_MilitaryLvUp)
	public void militaryLvUp(Player player, JsonMsg msg){
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.MilitaryRank)){
			player.sendErrorMsg(SysConstant.Military_Lock);
			return; 
		}
		Map<TankAttrType, Double> oldMap = tankAttrBiz.calMilitaryAttr(player);

		boolean isBuy = msg.getBoolean("isBuy");
		//自动强化，客户端循环调用该消息，直到没有资源或者玩家主动停止
		boolean result = commanderBiz.militarylvUp(player,isBuy);
		if(result){
			player.sendUserUpdateMsg();
		}
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_MilitaryLvUp);
		retMsg.addProperty("result",result);
		if(result) {
			retMsg.addProperty("changeAttr",TankAttrBiz.diffAttrMap(oldMap,tankAttrBiz.calMilitaryAttr(player)));
		}
		player.sendMsg(retMsg);
	}
	

	
	@MsgMethod ( MessageComm.C2S_SuperWeaponLvUp)
	public void superWeaponUpgrade(Player player, JsonMsg msg){
		int lv = player.playerCommander().getSuperWeaponLv();
		int superWeaponUpgrade = player.playerCommander().getSuperWeaponUpgrade();//进阶等级
		if(lv<= 0){
			player.sendErrorMsg(SysConstant.SuperWeapon_Lock);
			return; 
		}
		if(lv == commanderConfig.getSuperWeaponLvMax()){
			player.sendErrorMsg(SysConstant.Superweapon_Lv_Max);
			return; 
		}
		SuperWeaponExTemplate template = commanderConfig.getSuperWeaponMap(lv);
		if(superWeaponUpgrade<template.getLimit()){
			return;
		}
		List<Items> costItems = template.getCostItems(); 
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.SuperWeaponLv)){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return; 
		}
		player.playerCommander().addSuperWeaponLv();
		player.notifyObservers(ObservableEnum.SuperWeaponLv);
		player.notifyObservers(ObservableEnum.CHEquWeaponLv, lv, player.playerCommander().getSuperWeaponLv(), costItems);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_SuperWeaponLvUp,true);
	}
	@MsgMethod ( MessageComm.C2S_SuperWeaponUpgrade)
	public void superWeaponLvUp(Player player, JsonMsg msg){
		int lv = player.playerCommander().getSuperWeaponLv();
		int openLv = commValueConfig.getCommValue(CommonValueType.SuperWeaponUpgradeLimit);
		if(player.playerLevel().getLv()<openLv){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		
		if(lv<= 0){
			player.sendErrorMsg(SysConstant.SuperWeapon_Lock);
			return; 
		}
		int superWeaponUpgrade = player.playerCommander().getSuperWeaponUpgrade();
		int maxUpgrade = commanderConfig.getSuperWeaponUpgradeMax();
		if(superWeaponUpgrade >= maxUpgrade){
			player.sendErrorMsg(SysConstant.Superweapon_Lv_Max);
			return; 
		}
		SuperWeaponUpgradeExtrTemplate template = commanderConfig.getSuperWeaponUpgrade(superWeaponUpgrade);
		List<Items> costItems = template.getCosts(); 
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.SuperWeaponLv)){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		player.playerCommander().addSuperWeaponUpgrade();
		player.notifyObservers(ObservableEnum.SuperWeaponUpgrade);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_SuperWeaponUpgrade,true);
	}
	
	/**
	 * militaryProjectLvUp:(军工升级). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
	@MsgMethod ( MessageComm.C2S_MilitaryProjectLvUp)
	public void militaryProjectLvUp(Player player, JsonMsg msg){
		String papers = msg.getString("papers");
		
		if(StringUtils.isEmpty(papers) || player.playerLevel().getLv() >= commanderConfig.getMilitaryProMaxLv()){
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return; 
		}
		int minLv = functionConfig.getFunctionTemplate(PlayerFunctionType.MilitaryProject.getType()).getLevel();
		if(player.playerLevel().getLv()<minLv){
			player.sendErrorMsg(SysConstant.Player_Level_Not_Enough);
			return; 
		}
		List<Integer> listPapers = StringUtil.splitStr2IntegerList(papers, ",");
		List<Items> itemList = player.playerTankPaper().getPaperList(listPapers);
		//消耗资源
		if(!itemBiz.checkItemEnoughAndSpend(player, itemList, LogType.MilitaryProjectCost)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return; 
		}
		long exp = tankConfig.getPaperExp(itemList);
		int oldLv = player.playerCommander().getMilitaryProject().getLv();
		player.playerCommander().getMilitaryProject().addExp(exp);
		player.playerCommander().SetChanged();
		int newLv = player.playerCommander().getMilitaryProject().getLv();
		if(newLv != oldLv) {
			player.notifyObservers(ObservableEnum.MilitaryProjectLvUp);
		}
		player.notifyObservers(ObservableEnum.CHEquMilitaryProjectLvUp, itemList, oldLv, newLv);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_MilitaryProjectLvUp,true);
	}

}
  
