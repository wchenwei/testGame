/**  
 * Project Name:SLG_GameFuture.
 * File Name:MailBiz.java  
 * Package Name:com.hm.action.mail  
 * Date:2017年12月18日下午5:23:27  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */  
  
package com.hm.action.tank.biz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.bag.BagBiz;
import com.hm.action.commander.biz.CommanderBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.tank.enums.TankDetailMsg;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankSkillConfig;
import com.hm.config.excel.temlate.TankLevelTemplate;
import com.hm.config.excel.templaextra.DriverLvTemplate;
import com.hm.config.excel.templaextra.DriverTemplate;
import com.hm.config.excel.templaextra.StarLevelTemplateImpl;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.tank.Driver;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.model.tank.TankVo;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.ItemConstant;
import com.hm.util.ItemUtils;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.troop.TankArmy;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**  
 * ClassName: TankBiz. <br/>  
 * date: 2017年12月18日 下午5:23:27 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Biz
public class TankBiz{
	@Resource
	private TankConfig tankConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private BagBiz bagBiz; 
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private LogBiz logBiz;
	@Resource
	private TankAttrBiz tankAttrBiz;
	@Resource
	private TankSkillConfig tankSkillConfig;
	@Resource
	private CommanderBiz commanderBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private CommValueConfig commValueConfig;
	
	/**
	 * 发送坦克更新
	 *
	 * @author yanpeng 
	 * @param player
	 * @param tankIds  
	 * @param combatIsChange 
	 *
	 */
	public void updateTank(Player player, List<Integer> tankIds, boolean combatIsChange) {
		if(combatIsChange){
			//如果本次更新会引起战力变化则检查该坦克的战力变化是否会引起其他坦克的战力变化(如奇兵)
			tankIds = getTankIdsCombatChange(player, tankIds);
			//对坦克进行品质排序
			tankIds = getTankIdsRareSort(tankIds);
		}
		List<Tank> tankList = Lists.newArrayList();
		Map<Integer,TankAttr> typeAdd = Maps.newHashMap();
		long combat = 0; 
		if(combatIsChange) {
			typeAdd = tankAttrBiz.getTankAttrPlayerByTankType(player);
			combat =  tankAttrBiz.getPlayerCombat(player);
		}
		final long playerCombat = combat; 
		final Map<Integer,TankAttr> tempAttr = typeAdd;
		tankIds.forEach(tankId -> {
			Optional<Tank> tank = Optional.ofNullable(player.playerTank().getTank(tankId));
			tank.ifPresent(t -> {
				if(combatIsChange) {
					calTankCombat(player, t,tempAttr,playerCombat);
				}
				tankList.add(t);
			});
		});
		player.playerTank().SetChanged();
		player.sendMsg(MessageComm.S2C_TankUpdate, tankList);
		//坦克战力发生变化
		if(combatIsChange){
			player.notifyObservers(ObservableEnum.TankCombatChange);
		}
	}
	
	
	/**
	 * 更新所有坦克
	 *
	 * @author yanpeng 
	 * @param player
	 * @param
	 *
	 */
	public  void updateTankAll(Player player) {
		Map<Integer,TankAttr> typeAdd = tankAttrBiz.getTankAttrPlayerByTankType(player);
		List<Tank> tankList = getTankRareSort(player,player.playerTank().getTankList());
		long playerCombat = tankAttrBiz.getPlayerCombat(player);
		//把全加属性和按类型加的属性放到一起
		tankList.forEach(tank->calTankCombat(player, tank,typeAdd,playerCombat));
		player.playerTank().SetChanged();
		player.sendMsg(MessageComm.S2C_TankUpdate, tankList);
		//坦克战力发生变化
		player.notifyObservers(ObservableEnum.TankCombatChange);
	}
		

	/**
	 * 增加坦克
	 *
	 * @author yanpeng 
	 * @param player  
	 * @param tankId
	 *
	 */
	public void addTank(Player player,int tankId,LogType logType){
		TankSetting template = tankConfig.getTankSetting(tankId); 
		if(template == null) return; 
		if(!player.playerTank().isHaveTank(tankId)){
			Tank tank = createTank(player,tankId);
			player.playerTank().addTank(tank);
			//记录日志
			logBiz.addGoods(player, tankId, 1, ItemType.TANK.getType(), logType);
			player.sendMsg(MessageComm.S2C_TankAdd,tank);
			player.notifyObservers(ObservableEnum.TankAdd, tankId,logType);
		}else{ 
			//转为坦克图纸
			int paperCount = tankConfig.getTankStarTemplate(template.getStar()).getRepeat_paper();
			int paperId = template.getPaper_id(); 
			itemBiz.addItem(player, ItemType.PAPER, paperId, paperCount, LogType.TankToPapar);
		}
	}
	
	/**
	 * 获取玩家所有坦克
	 *
	 * @author yanpeng 
	 * @param player  
	 *
	 */
	public void loadTankList(Player player){
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_TankList);
		msg.addProperty("playerTankList", player.playerTank().getTankList());
		player.sendMsg(msg);
	}
	
	/**
	 * 生成坦克
	 *
	 * @author yanpeng 
	 * @param tankId
	 * @return  
	 *
	 */
	public Tank createTank(Player player,int tankId) {
		Tank tank = new Tank();
		TankSetting temp = tankConfig.getTankSetting(tankId);
		if(temp != null) {
			tank.setId(tankId);
			tank.setLv(1);
			tank.setStar(temp.getStar());
			tank.initSkill(temp.getSkillList());
			checkTankSkill(tank);
			tank.initDriver(tankConfig.getDriverTemplate(tankId));
			calTankCombat(player, tank, tankAttrBiz.getTankAttrPlayerByTankType(player),tankAttrBiz.getPlayerCombat(player));
		}
		return tank;
	}
	
	/**
	 * 计算坦克属性
	 * 
	 * 坦克攻击 = 坦克初始攻击配表值*资质星级系数 + （等级 - 1） * 资质星级系数*坦克攻击成长配表值
	 * 坦克防御 = 坦克初始防御配表值*资质星级系数 + （等级 - 1） * 资质星级系数*坦克防御成长配表值
	 * 坦克耐久 = 坦克初始耐久配表值*资质星级系数 + （等级 - 1） * 资质星级系数 *坦克耐久成长配表值
	 * 坦克战斗外总攻击 = （坦克攻击 + 各种养成加成攻击固定值求和 ）* （1 + 各种养成加成攻击%求和） 
	 * 坦克战斗外总防御 = （坦克防御 + 各种养成加成防御固定值求和）* （1 + 各种养成加成防御%求和） 
	 * 坦克战斗外总耐久 = （坦克防御 + 各种养成加成耐久固定值求和 ）* （1 + 各种养成加成耐久%求和） 
	 * 坦克战斗外总命中 =   (坦克初始命中配表值 + 各种养成加成命中固定值求和）*（1 + 各种养成命中加成%求和）
	 * 坦克战斗外总闪避 =   (坦克初始闪避配表值 + 各种养成加成闪避固定值求和）*（1 + 各种养成闪避加成%求和）
	 * 坦克战斗外总暴击 =   (坦克初始暴击配表值 + 各种养成加成暴击固定值求和）*（1 + 各种养成暴击加成%求和）
	 * 坦克战斗外总防暴 =   (坦克初始防暴配表值 + 各种养成加成防暴固定值求和）*（1 + 各种养成防暴加成%求和）
	 * 坦克战斗外总暴伤% =   坦克初始配表值% + 各种养成暴伤加成%求和
	 * 坦克战斗外总暴伤抵抗% = 坦克初始配表值%+各种养成暴伤抵抗加成%求和
	 * 坦克战斗外总增伤% = 各种养成增伤加成%求和
	 * 坦克战斗外总减伤% = 各种养成减伤加成%求和
	 * 坦克战斗外总反伤% = 各种养成反伤加成%求和
	 * 各种养成加成求和 =  坦克进阶对应加成 +  坦克羁绊加成 + 坦克勋章加成 + 坦克车长加成 + 指挥官旗帜加成 + 指挥官军阶加成 + 指挥官指挥车加成 + 指挥官装备加成 + 总战力等级加成
	 * @author yanpeng 
	 * @param tank  
	 *
	 */
	public void calTankAttr(Player player,Tank tank,TankAttr playerAttr,TankAttr attrAdd){
		//各种养成加成求和
		attrAdd.addAttr(playerAttr);
		//新的坦克属性
		Map<Integer, Double> tankAttrMap = Maps.newHashMap();
		//坦克初始配表值
		Map<TankAttrType,Double> attrInit = tankConfig.getTankSetting(tank.getId()).getTankAttrInit();
		TankSetting temp = tankConfig.getTankSetting(tank.getId());
		//坦克战斗外总攻击
		double starRate = tankConfig.getStarRate(tank.getStar(), temp.getRare());
		double atkBase = (attrInit.get(TankAttrType.ATK) + (tank.getLv()-1) * temp.getAtk_add()) * starRate;
		double atk = (atkBase+attrAdd.getAttrValue(TankAttrType.ATK))*(1+attrAdd.getAttrValue(TankAttrType.AtkPer));
		tankAttrMap.put(TankAttrType.ATK.getType(), getTwoDouble(atk));
		//坦克战斗外总防御
		double defBase = (attrInit.get(TankAttrType.DEF) + (tank.getLv()-1) * temp.getDef_add()) * starRate;
		double def = (defBase+attrAdd.getAttrValue(TankAttrType.DEF))*(1+attrAdd.getAttrValue(TankAttrType.DefPer));
		tankAttrMap.put(TankAttrType.DEF.getType(), getTwoDouble(def));
		//坦克战斗外总耐久 
		double hpBase = (attrInit.get(TankAttrType.HP) + (tank.getLv()-1) * temp.getHp_add()) * starRate;
		double hp = (hpBase+attrAdd.getAttrValue(TankAttrType.HP))*(1+attrAdd.getAttrValue(TankAttrType.HpPer));
		tankAttrMap.put(TankAttrType.HP.getType(), getTwoDouble(hp));
		//坦克战斗外总命中,坦克战斗外总闪避,坦克战斗外总暴击,坦克战斗外总防暴
		for(int i=TankAttrType.HIT.getType();i<=TankAttrType.CritDef.getType();i++){
			TankAttrType attrType1 = TankAttrType.getType(i);
			TankAttrType attrType2 = TankAttrType.getType(i+7);
			double value = (attrInit.get(attrType1)+attrAdd.getAttrValue(attrType1))*(1+attrAdd.getAttrValue(attrType2));
			tankAttrMap.put(attrType1.getType(), getTwoDouble(value));
		}
		//坦克战斗外总暴伤%,坦克战斗外总暴伤抵抗% 
		for(int i=TankAttrType.CritDamPer.getType();i<=TankAttrType.CritResPer.getType();i++){
			TankAttrType attrType = TankAttrType.getType(i);
			double value = (attrInit.get(attrType)+attrAdd.getAttrValue(attrType));
			tankAttrMap.put(attrType.getType(), getTwoDouble(value));
		}
		//坦克战斗外总增伤%,坦克战斗外总减伤%,坦克战斗外总反伤%
		for(int i=TankAttrType.AddAtkPer.getType();i<=TankAttrType.BackAtkPer.getType();i++){
			TankAttrType attrType = TankAttrType.getType(i);
			double value = attrAdd.getAttrValue(attrType);
			tankAttrMap.put(attrType.getType(), getTwoDouble(value));
		}
		//技能增强
		tankAttrMap.put(TankAttrType.AddSkillPer.getType(), attrAdd.getAttrValue(TankAttrType.AddSkillPer));
		//部队回复加成属性
		tankAttrMap.put(TankAttrType.TroopRecoverHp.getType(), attrAdd.getAttrValue(TankAttrType.TroopRecoverHp));
		
		for(int i=TankAttrType.AtkCd.getType();i<=TankAttrType.FirstAtkCd.getType();i++){
			TankAttrType attrType = TankAttrType.getType(i);
			tankAttrMap.put(attrType.getType(), getTwoDouble(attrInit.get(attrType)));
		}
		//设置新的坦克属性
		tank.setTotalAttrMap(tankAttrMap);
	}
	
	/**
	 * 计算坦克战力
	 * 
	 * 坦克战斗力公式 = 攻击 x 攻击战力系数 + 防御 x 防御战力系数 + 耐久 x 耐久战力系数 + 命中 x 命中战力系数 
	 * + 闪避 x 闪避战力系数 + (暴击-配表初值) x 暴击战力系数 + 防暴 x 防暴战力系数 +(暴伤%-配表初值) x 暴伤战力系数
	 *  + 暴伤抵抗% x 暴伤抵抗战力系数 + 增伤% x 增伤战力系数 +  减伤% x 减伤战力系数 
	 *  + 技能增强% x 技能增强战力系数  +  技能1等级 x 技能1的每级战力 + 技能2 x 技能2的每级战力 
	 *  + 技能3 x 技能3的每级战力 + 技能4 x 技能4的每级战力+车长技能战斗力
	 *
	 * @author yanpeng 
	 * @param player
	 * @param tank  
	 *
	 */
	public long calTankCombat(Player player, Tank tank,Map<Integer, TankAttr> typeAdd,long playerCombat) {
		//属性战力
		long combat = calTankAttrCombat(player, tank,typeAdd);
		// 技能战力
		combat += calTankSkillCombat(tank);
		//玩家战力
		combat += playerCombat; 
		tank.setCombat(combat);
		return combat;
	}
	
	private long calTankAttrCombat(Player player, Tank tank,Map<Integer, TankAttr> typeAdd){
		TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
		long combat = 0; 
		TankAttr attrAdd = tankAttrBiz.getTankAttrSelf(player, tank);
		TankAttr playerAttr = typeAdd.get(tankSetting.getType());
		calTankAttr(player, tank, playerAttr,attrAdd);
		// 坦克初始配表值
		Map<TankAttrType, Double> attrInit = tankSetting.getTankAttrInit();
		// 属性部分
		Map<TankAttrType, Integer> powerRateMap = tankConfig.getTankPowerRateMap();
		for(Map.Entry<TankAttrType, Integer> entry :powerRateMap.entrySet()) {
			TankAttrType attrType = entry.getKey();
			double attr = tank.getTotalAttr(attrType);
			if(attr >0){
				if(attrType == TankAttrType.CRIT) {
					attr -= attrInit.get(TankAttrType.CRIT);
				}else if(attrType == TankAttrType.CritDamPer) {
					attr -= attrInit.get(TankAttrType.CritDamPer);
				}
				combat += attr * entry.getValue();
			}
		}
		// + 技能增强% x 技能增强战力系数
		combat += (attrAdd.getAttrValue(TankAttrType.AddSkillPer) + playerAttr.getAttrValue(TankAttrType.AddSkillPer))
				* powerRateMap.get(TankAttrType.AddSkillPer);
		return combat;
	}
	
	/**
	 * 计算坦克技能战力
	 *
	 * @author yanpeng 
	 * @param tank
	 * @return  
	 *
	 */
	private long calTankSkillCombat(Tank tank){
		long combat = 0; 
		List<Integer> skillList = tankConfig.getTankSetting(tank.getId()).getSkillList();
		for(Integer id :skillList) {
			combat += tank.getSkillLv(id) * tankSkillConfig.getSkillSetting(id).getSkill_power();
		}
		return combat; 
	}

	public int getPlayerTankMaxLv(Player player, Tank tank){
		int lvByReLv = tankConfig.getTankMaxLvByReLv(tank.getReLv());
		int lvByMilitaryLv = tankConfig.getPlayerMaxTankLvByMilitaryLv(player.playerCommander().getMilitaryLv());
		return Math.min(lvByReLv, lvByMilitaryLv);
	}

	/**
	 * 坦克升级
	 *
	 * @author yanpeng 
	 * @param player
	 * @param tankId
	 *
	 */
	public boolean lvUpTank(Player player, int tankId) {
		Tank tank = player.playerTank().getTank(tankId);
		if (tank != null && tank.getLv() < getPlayerTankMaxLv(player, tank)){
			return lvUpTank(player,tank);
		}
		return false;
	}
	
	
	/**
	 * 坦克升级
	 */
	public boolean lvUpTank(Player player, Tank tank) {
		tank.lvUp();
		checkTankSkill(tank);
		player.notifyObservers(ObservableEnum.TankLv, tank.getId(), 1);
		player.playerTank().SetChanged();
		return true;
	}
	
	/**
	 * 坦克一键升级
	 *
	 * @author yanpeng 
	 * @param player
	 * @param tank  
	 *
	 */
	public void maxLvUpTank(Player player,Tank tank){
		int itemId = commValueConfig.getCommValue(CommonValueType.TankLvUpCostItemId);

		int maxLv = getPlayerTankMaxLv(player, tank);
		long lvExp = tankConfig.getTankLevelTemplate(tank.getLv()).getExp_total();
		int oldLv = tank.getLv();
		for (int i = maxLv; i > oldLv; i--) {
			TankLevelTemplate levelTemplate = tankConfig.getTankLevelTemplate(i);
			long diffCount = levelTemplate.getExp_total() - lvExp;
			// 扣除道具
			if(itemBiz.checkItemEnoughAndSpend(player, itemId, diffCount, ItemType.ITEM, LogType.TankLvUp.value(tank.getId()))) {
				tank.setLv(i);
				break;
			}
		}
		if(tank.getLv() > oldLv){
			checkTankSkill(tank);
			player.playerTank().SetChanged();
			player.notifyObservers(ObservableEnum.TankLv, tank.getId(),tank.getLv()-oldLv);
		}
	}

	/**
	 * 坦克改造
	 *
	 * @author yanpeng 
	 * @param player
	 * @param tank  
	 *
	 */
	public void reform(Player player,Tank tank){
		tank.reform();
		player.playerTank().SetChanged();
		player.notifyObservers(ObservableEnum.TankReform, tank.getId());
	}

	public void unlockStartNode(Player player, Tank tank, StarLevelTemplateImpl nodeStarLevelTemplate) {
		if (nodeStarLevelTemplate.isStarUp()){
			tank.addStar();
			player.notifyObservers(ObservableEnum.TankStarUp, tank.getId());
		}else {
			tank.addStarNode();
		}
		player.playerTank().SetChanged();
		player.notifyObservers(ObservableEnum.TankUnlockStarNode, tank.getId());
	}
	
	/**
	 * 更新坦克状态
	 *
	 * @author yanpeng 
	 * @param player
	 * @param tankIds
	 * @param state  
	 *
	 */
	public void updateTankState(Player player, List<Integer> tankIds, TankStateType state) {
		tankIds.forEach(t -> player.playerTank().getTank(t).setState(state));
		player.notifyObservers(ObservableEnum.TankListState, tankIds);
	}
	
	/**
	 * 车长升级
	 *
	 * @author yanpeng
     * @param player
     * @param tank
     *
	 */
	public void driverLvUp(Player player, Tank tank, int newDriverLv){
		tank.getDriver().setLv(newDriverLv);
		player.playerTank().SetChanged();
		player.notifyObservers(ObservableEnum.DriverLv, tank.getId());
	}


	public int getDriverUpLvAndCalCost(Player player, Tank tank, List<Items> totalCostItems, int upLvMax) {
		int curLv = tank.getDriver().getLv();
		for (int lv = curLv; lv < upLvMax; lv++) {
			List<Items> newTotalCost = getDriverLvTotalCost(tank, totalCostItems, lv);
			if (!itemBiz.checkItemEnough(player, newTotalCost)){
				return lv - 1;
			}
			totalCostItems = newTotalCost;
		}
		return upLvMax;
	}

	private List<Items> getDriverLvTotalCost(Tank tank, List<Items> costItems, int lv){
		List<Items> costs = Lists.newArrayList(costItems);

		DriverLvTemplate temp = tankConfig.getDriverLvTemplate(tank);
		costs.addAll(temp.getItemCost());
		//进阶需要消耗魂魄
		if(temp.getCost_soul()>0){
			int soulId = tankConfig.getDriverTemplate(tank.getId()).getSoul();
			int cost = temp.getCost_soul();
			Items items = new Items(soulId,cost,ItemType.ITEM.getType());
			costs.add(items);
		}
		return ItemUtils.mergeItemList(costItems);
	}

	
	/**
	 * 获取玩家战力最强的坦克
	 *
	 * @author yanpeng 
	 * @param player
	 * @param top
	 * @return  
	 *
	 */
	public List<Tank> getTankListTopCombat(BasePlayer player, int top) {
		return getTankListTopCombat(player,top,false);
	}
	/**
	 * 获取玩家战力最强的坦克
	 *
	 * @author yanpeng 
	 * @param player
	 * @param top
	 * @Param tankSoldier true:包含奇兵, false 包含奇兵
	 * @return  
	 *
	 */
	public List<Tank> getTankListTopCombat(BasePlayer player, int top,boolean tankSoldier) {
		List<Tank> tankList = Lists.newArrayList();
		if(player == null) {
			return tankList;
		}
		if(tankSoldier) {
			return player.playerTank().getTankList().stream()
					.sorted(Comparator.comparing(Tank::getCombat).reversed())
					.limit(top)
					.collect(Collectors.toList());
		}
		return player.playerTank().getTankList().stream().filter(t ->t.getMainTank()<=0)
				.sorted(Comparator.comparing(Tank::getCombat).reversed())
				.limit(top)
				.collect(Collectors.toList());
	}
	
	/**
	 * 获取玩家战力最弱的坦克
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public List<Tank> getTankListLowCombat(BasePlayer player, int low) {
		List<Tank> tankList = Lists.newArrayList();
		if(player == null) {
			return tankList;
		}
		return player.playerTank().getTankList().stream().filter(t ->t.getMainTank()<=0)
				.sorted(Comparator.comparing(Tank::getCombat))
				.limit(low)
				.collect(Collectors.toList());
	}
	
	/**
	 * 获取玩家几个坦克的战力和
	 */
	public long getTankCombat(Player player,List<Integer> tankIds){
		return tankIds.stream().mapToLong(e -> player.playerTank().getTank(e).getCombat()).sum();
	}
	
	/**
	 * 获取玩家几个坦克的战力最高战力
	 */
	public long getTankMaxCombat(Player player,List<Integer> tankIds){
		return tankIds.stream().mapToLong(e -> player.playerTank().getTank(e).getCombat()).max().getAsLong();
	}
	
	private double getTwoDouble(double value){
		BigDecimal big = new BigDecimal(value);
		return big.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();     
	}

	/**
	 * 一键布阵逻辑（抄客户端的逻辑）
	 * @param tanks
	 * @return
	 */
	public List<TankArmy> createTankPos(List<Tank> tanks){
		Map<Integer,Integer> map = Maps.newConcurrentMap();
		for(Tank tank:tanks){
			putTankData(tank,map);
		}
		adjustPos(map);
		List<TankArmy> tankArmyList = Lists.newArrayList();
		for(Map.Entry<Integer, Integer> entry:map.entrySet()){
			int pos = entry.getKey();
			int tankId = entry.getValue();
			tankArmyList.add(new TankArmy(pos,tankId));
		}
		return tankArmyList;
	}
	//把坦克放到相应的位置
	public void putTankData(Tank tank,Map<Integer,Integer> map) {
		TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
		//位置优先级，如：防御坦克优先站位0,次优先位置3,攻击坦克优先站位3，次优先站位0,支援和辅助优先站位6，次优先站位3
		int[][] indexPriority = new int[][]{{0,3},{3,0},{6,3},{6,3}};
		//先找优先位置往下找，找不到再找次优先位置
		for(int i=1;i<=2;i++){
			int startIndex = indexPriority[tankSetting.getType()-1][i-1];
			//找到空位
			int emptyIndex = getEmptyIndex(startIndex, map);
			if(emptyIndex >= 0){
				map.put(emptyIndex, tank.getId());
				return;
			}
		}
	}

	private int getEmptyIndex(int startIndex, Map<Integer, Integer> map) {
		//优先找中间
		if(!map.containsKey(startIndex+1)){
			return startIndex+1;
		}
		//再找左侧
		if(!map.containsKey(startIndex)){
			return startIndex;
		}
		//再找右侧
		if(!map.containsKey(startIndex+2)){
			return startIndex+2;
		}
		return -1;
	}
	//调整一排的站位
	private void  adjustPos(Map<Integer,Integer> map){
		for(int i=0;i<=6;i=i+3){
			int num =0;//本行放置了几个坦克
			for(int j=0;j<=2;j++){
				if(map.containsKey(i+j)){
					num =num+1;
				}
			}
			//如果本行放置两个坦克则将两个坦克放在两边
			if(num==2 && !map.containsKey(i+2)){
				int tankId = map.remove(i+1);
				map.put(i+2, tankId);
			}
		}
	}

	
	public TankVo getMaxTank(BasePlayer player){
		List<Tank> tanks = getTankListTopCombat(player, 1);
		if(tanks.isEmpty()){
			return null;
		}
		TankVo tankVo = new TankVo(tanks.get(0));
		return tankVo;
	}

	//获取坦克升星或突破所需的图纸(图纸不足用万能图纸和超级万能图纸抵消)
	public List<Items> checkSpendItems(Player player, TankSetting tankSetting, int paperId, long paperCost, int type) {
		List<Items> itemList = Lists.newArrayList();
		//拥有的坦克图纸数量
		long tankPaper = player.playerTankPaper().getItemCount(paperId);
		long costPaper = Math.min(tankPaper, paperCost);
		itemList.add(new Items(paperId,costPaper,ItemType.PAPER.getType()));
		paperCost -= costPaper;
		if(paperCost <= 0) {
			//本身图纸足够
			return itemList;//只有普通消耗
		}
		if (type > 0){// 使用万能图纸
			//-----S及S以下坦克（不能使用超级万能图纸）
			if(tankSetting.getRare() <= TankRareType.SR.getType()) {
				itemList.add(new Items(ItemConstant.COM_PAPER,paperCost,ItemType.ITEM.getType()));
				return itemList;
			}
			//-----SS坦克
			if(tankSetting.getRare()==TankRareType.SSR.getType()){
				//计算超级图纸数量
				itemList.add(new Items(ItemConstant.SUPER_PAPER,paperCost,ItemType.ITEM.getType()));
				return itemList;
			}
			if(tankSetting.getRare()==TankRareType.SSSR.getType()){
				//计算超级图纸数量
				itemList.add(new Items(ItemConstant.SENIOR_PAPER,paperCost,ItemType.ITEM.getType()));
				return itemList;
			}
		}
		return itemList;
	}

	/**
	 * 根据tank rare 构建对应级别万能图纸道具
	 *
	 * @param tankRare
	 * @param cnt
	 * @return
	 */
	public List<Items> checkSpendItems(int tankRare, long cnt) {
		Items items = null;
		if (tankRare <= TankRareType.SR.getType()) {
            items = new Items(ItemConstant.COM_PAPER, cnt, ItemType.ITEM.getType());
		} else if (tankRare == TankRareType.SSR.getType()) {
			items = new Items(ItemConstant.SUPER_PAPER, cnt, ItemType.ITEM.getType());
		} else if (tankRare == TankRareType.SSSR.getType()) {
			items = new Items(ItemConstant.SENIOR_PAPER, cnt, ItemType.ITEM.getType());
		}
		List<Items> itemList = Lists.newArrayList();
		if (items != null) {
			itemList.add(items);
		}
		return itemList;
	}

	//对tank的品质进行排序(从低到高)
	private List<Integer> getTankIdsRareSort(List<Integer> tankIds){
		if(tankIds.size()<=1){
			return tankIds;
		}
		tankIds = tankIds.stream().map(t ->tankConfig.getTankSetting(t)).sorted(Comparator.comparing(TankSetting::getRare)).map(t ->t.getId()).collect(Collectors.toList());
		return tankIds;
	}
	
	public List<Tank> getTankRareSort(Player player,List<Tank> tanks){
		if(tanks.size()<=1){
			return tanks;
		}
		return tanks.stream().map(t ->tankConfig.getTankSetting(t.getId())).sorted(Comparator.comparing(TankSetting::getRare)).map(t ->player.playerTank().getTank(t.getId())).collect(Collectors.toList());
	}
	
	//获取tankIds中战力改变后会引起战力变化的坦克id
	private List<Integer> getTankIdsCombatChange(Player player,List<Integer> tankIds){
		List<Integer> ids = Lists.newArrayList();
		tankIds.forEach(t ->{
			Tank tank = player.playerTank().getTank(t);
			if(tank!=null&&tank.getMainTank()>0){
				ids.add(tank.getMainTank());
			}
		});
		if(ids.size()>0){
			tankIds.addAll(ids);
		}
		return tankIds;
	}
	
	
	/**
	 * 获取玩家tank的改造属性。tank_base表中的reformtype, reform_base表
	 * @Title: getTankReformQuality   
	 * @Description: 
	 * @param player
	 * @param tank
	 * @return      
	 * @return: int      
	 * @throws
	 */
	public int getTankReformQuality(Player player, Tank tank) {
		if(tank==null){
			return 0;
		}
		TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
		int reformType = tankSetting.getReform_type();
		int tankQuality = tankConfig.getTankReformQuality(reformType, tank.getReLv());
		return tankQuality;
	}

    public int getDriverLv(Player player, int driverId) {
        Driver driver = player.playerTank().getTankList()
                .stream().filter(e -> e.getDriver() != null && e.getDriver().getId() == driverId)
                .map(e -> e.getDriver())
                .findFirst().orElse(null);
        return driver != null ? driver.getLv() : 0;
    }

    public Map<Integer, Object> getTankMsg(Player player, int tankId) {
        Map<Integer, Object> resultMap = Maps.newHashMap();
        for (TankDetailMsg temp : TankDetailMsg.values()) {
            resultMap.put(temp.getType(), temp.getData(player, tankId));
        }
        return resultMap;
    }

//	public boolean driverSkillLvUpMax(Player player,Tank tank,int driverSkillId){
//		int lv = tank.getDriver().getSkillLv(driverSkillId);
//		long cost = 0;
//		int endLv = lv;
//		int maxLv = Math.min(tankConfig.getDriverSkillLvMax(),tank.getDriver().getLv() * 2);
//		for(int i=lv;i<maxLv;i++){
//			DriverSkillLevelTemplate template = tankConfig.getDriverSkillLevleTemplate(i);
//			if(template==null){
//				break;
//			}
//			long oneCost = tankConfig.getDriverSkillLevleTemplate(i).getSkill_cash();
//			if(!playerBiz.checkPlayerCurrency(player,CurrencyKind.Cash,cost+oneCost)){
//				break;
//			}
//			cost += oneCost;
//			endLv++;
//		}
//		if(cost>0&&!playerBiz.checkAndSpendPlayerCurrency(player,CurrencyKind.Cash,cost,LogType.ActivityTask)){
//			return false;
//		}
//		player.notifyObservers(ObservableEnum.CHTankDriverSkillLvUp, tank.getId(), driverSkillId,
//				endLv, CurrencyKind.Cash.getIndex(), cost, player.playerCurrency().get(CurrencyKind.Cash));
//		if(endLv<=lv){
//			return false;
//		}
//
//
//		tank.getDriver().setSkillLv(driverSkillId, endLv);
//		DriverSkillType skillType = DriverSkillType.getType(tankConfig.getDriverSkillExtraTemplate(driverSkillId)
//				.getType());
//		if(skillType == DriverSkillType.One) {
//			player.notifyObservers(ObservableEnum.DriverSkillLv1, tank.getId());
//		}else if(skillType == DriverSkillType.All) {
//			player.notifyObservers(ObservableEnum.DriverSkillLv2);
//		}else if(skillType == DriverSkillType.War){
//			player.notifyObservers(ObservableEnum.DriverSkillLv3, tank.getId());
//		}
//		return true;
//	}

	public void checkTankSkill(Tank tank){
		//解锁坦克技能
		int lv = tank.getLv();
		int[] unlockSkillLvs = commValueConfig.getConvertObj(CommonValueType.TankSkillUnlockLvs);
		TankSetting temp = tankConfig.getTankSetting(tank.getId());
		for (int i = 0; i < unlockSkillLvs.length; i++) {
			int unlockLv = unlockSkillLvs[i];
			if (lv >= unlockLv){
				tank.skillLvUp(temp.getSkillList().get(i), lv);
			}
		}
	}
}



