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
import com.hm.action.agent.AgentBiz;
import com.hm.action.commander.biz.AircraftCarrierBiz;
import com.hm.action.commander.biz.CommanderBiz;
import com.hm.action.guild.biz.GuildFactoryBiz;
import com.hm.action.mastery.biz.MasteryBiz;
import com.hm.action.memorialHall.biz.MemorialHallAttrBiz;
import com.hm.action.militarylineup.MilitaryLineupBiz;
import com.hm.action.passenger.biz.PassengerBiz;
import com.hm.action.player.PlayerWarCraftBiz;
import com.hm.action.strength.StrengthBiz;
import com.hm.config.EquipmentConfig;
import com.hm.config.excel.CommanderConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.WarCraftConfig;
import com.hm.config.excel.temlate.WarCraftSkillTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.TankAttrType;
import com.hm.enums.TankType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Equipment;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**  
 * ClassName: TankAttrBiz. <br/>  
 * date: 2017年12月18日 下午5:23:27 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Biz
public class TankAttrBiz implements IObserver{
	@Resource
	private TankConfig tankConfig;
	@Resource
	private TankBiz tankBiz;
	@Resource
	private CommanderConfig commanderConfig; 
	@Resource
	private EquipmentConfig equipmentConfig;
	@Resource
	private TankFettersBiz tankFettersBiz;
	@Resource
	private TankSpecialBiz tankSpecialBiz;
	@Resource
	private AgentBiz agentBiz;
	@Resource
	private CommanderBiz commanderBiz;
	@Resource
	private PassengerBiz passengerBiz;
	@Resource
	private MilitaryLineupBiz militaryLineupBiz;
	@Resource
	private MagicReformBiz magicReformBiz;
	@Resource
	private MemorialHallAttrBiz memorialHallAttrBiz;
	@Resource
	private PlayerWarCraftBiz warCraftBiz;
	@Resource
	private WarCraftConfig warCraftConfig;
	@Resource
	private TankStrengthBiz tankStrengthBiz;
	@Resource
	private TankDriverAdvanceBiz driverAdvanceBiz;
	@Resource
	private GuildFactoryBiz guildFactoryBiz;
	@Resource
	private MasteryBiz masteryBiz;
	@Resource
	private TankSoldierBiz tankSoldierBiz;
	@Resource
	private TankJingzhuBiz tankJingzhuBiz;
	@Resource
	private AircraftCarrierBiz aircraftCarrierBiz;
	@Resource
	private ControlBiz controlBiz;
	@Resource
	private StrengthBiz strengthBiz;
	@Resource
	private TankMasterBiz tankMasterBiz;
	

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankExp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankUnlockStarNode, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankPartsCompose, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankReform, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankSkill, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankListLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankListExp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankListState, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.WarGodLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.DriverLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.DriverSkillLv1, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.DriverSkillLv2, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.DriverSkillLv3, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.FriendLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CarLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CarModelUnLock, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.SuperWeaponLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankHpUpdate, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankTechUpdate, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ComposeEqu, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.StrengthenEqu, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.RefineEqu, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeStone, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.StoneLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankEvolveStarUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeTitle, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MasteryLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankSpecialLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankSpecialChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Agent, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryProjectLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Passenger_Eq, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Passenger_LvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Passenger_StarUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Passenger_Culture, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankRecast, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryLineup, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MedalMaxLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MagicReform, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.SuperWeaponUpgrade, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MagicReformTransfer, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.EquLvUp, this);
		
		ObserverRouter.getInstance().registObserver(ObservableEnum.MemorialHallChapterLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MemorialHallChapterPhotoAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.WarCraftLvup, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.WarCraftSkillLvup, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankStrength, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankStrengthBreach, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankStrengthTaskChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankDriverAdChangeFetter, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankDriverAdChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ArmsChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.SpecialMedalStarOrLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankJingzhuAttrChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankJingzhuLvup, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.SoldierBind, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.SoldierUnBind, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.SoldierLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AutoDriveLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CarModelStarUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AircraftCarrierBuild, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AircraftCarrierLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AircraftCarrierEit, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AircraftStarUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeElement, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ElementLvUp, this, 1);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ControlTransfer, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AircraftIslandLvup, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AgentCenterLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MagicReformRest, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.StrengthChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.StrengthLvChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.StrengthSublimationChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.StrengthAttrChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankMasterScoreAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.DriverEvolveStarUp, this);
	}

	@SuppressWarnings ( "unchecked" )
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		List<Integer> tankIds = Lists.newArrayList();
		switch(observableEnum) {
			//坦克升级
			case TankLv :
			//坦克升星
			case TankUnlockStarNode:
			//配件合成
			case TankPartsCompose:
			//坦克改造
			case TankReform:
			//坦克技能升级
			case TankSkill:
			//车长升级
			case DriverLv:
			case DriverSkillLv1:
			//车长技能（类型3）升级
			case DriverSkillLv3:
			case FriendLv:
			case TankTechUpdate:
			case TankEvolveStarUp:
			case TankSpecialLvUp:
			case TankSpecialChange:
			case MagicReform://魔改
			case TankStrength://强化
			case TankDriverAdChange://坦克-车长-兵法
			case TankJingzhuAttrChange:
			case TankJingzhuLvup:
			case SoldierLvUp:
			case DriverEvolveStarUp:
				tankIds.add((int)(argv[0]));
				tankBiz.updateTank(player, tankIds, true);
				break;
			case AircraftStarUp:
			case StrengthLvChange:
			case StrengthSublimationChange:
			case StrengthAttrChange:
				if((boolean)argv[1]) {
					tankBiz.updateTankAll(player);
				}
				break;
			case SoldierBind:
			case SoldierUnBind:
				//子坦克战力不发生变化
				tankBiz.updateTank(player, Lists.newArrayList((int)(argv[1])), false);
				//主坦克战力发生变化
				tankBiz.updateTank(player, Lists.newArrayList((int)(argv[0])), true);
				break;
			//坦克经验变化
			case TankExp:
				tankIds.add((int)(argv[0]));
				tankBiz.updateTank(player, tankIds,true);
				break;
			//多个坦克升级
			case TankListLv:
			case MagicReformTransfer:
			case TankDriverAdChangeFetter://坦克-车长-兵法
			case ControlTransfer://中控转移
			case MagicReformRest:
				tankIds.addAll((List<Integer>)argv[0]);
				tankBiz.updateTank(player, tankIds, true);
				break; 
			//多个坦克加经验
			case TankListExp:
				tankIds.addAll((List<Integer>)argv[0]);
				tankBiz.updateTank(player, tankIds,false);
				break; 
			//坦克状态
			case TankListState:
			case TankHpUpdate:
				tankIds.addAll((List<Integer>)argv[0]);
				tankBiz.updateTank(player, tankIds,false);
				break;
			//影响全部坦克
			case WarGodLv:
			case DriverSkillLv2:
			case CarLv:
			case CarModelUnLock:
			case SuperWeaponLv:
			case ChangeTitle:
			case Agent:
			case MilitaryProjectLvUp:
			case MilitaryLineup:
			case MedalMaxLvUp://勋章升级
			case SuperWeaponUpgrade:
			case MemorialHallChapterLv:
			case MemorialHallChapterPhotoAdd:
			case WarCraftSkillLvup:
			case WarCraftLvup:
			case TankStrengthTaskChange:
			case ArmsChange:
			case SpecialMedalStarOrLvUp:
			case AutoDriveLvUp:
			case CarModelStarUp:
			case AircraftCarrierLvUp:
			case AircraftCarrierBuild:
			case AircraftCarrierEit:
			case AircraftIslandLvup://航母--舰岛
			case AgentCenterLvUp:
			case StrengthChange:
			case TankMasterScoreAdd:
			case TankRecast://重铸
			case ComposeEqu:
			case EquLvUp:
			case StrengthenEqu:
			case RefineEqu:
			case ChangeStone:
			case StoneLvUp:
			case MilitaryLvUp:
				tankBiz.updateTankAll(player);
				break;
			case MasteryLvUp:
				//重新计算光环
				player.playerMastery().calCircleLv();
				tankBiz.updateTankAll(player);
				break;
			case FunctionUnlock:
				doFuntionUnlock(player, (int)argv[0]);
				break;
			case Passenger_Eq:
				passengerBiz.calPassengerCircle(player,(int)(argv[0]));
				tankIds.add((int)(argv[0]));
				tankBiz.updateTank(player, tankIds, true);
				break;
			case Passenger_Culture:
			case Passenger_LvUp:
			case Passenger_StarUp:
				int tankId = (int)(argv[0]);
				if(tankId>0){
					tankIds.add(tankId);
					tankBiz.updateTank(player, tankIds, true);
				}
				break;
			case TankStrengthBreach:
				tankIds.add((int)(argv[0]));
				tankBiz.updateTank(player, tankIds, false);
				break;
			case ElementLvUp:
			case ChangeElement:
				tankIds.add((int)(argv[0]));
				//计算激活的技能
				controlBiz.calControlSkill(player,(int)(argv[0]));
				tankBiz.updateTank(player, tankIds, true);
				break;
			default:
				break;
		}
	}
	
	private void doFuntionUnlock(Player player,int functionId) {
		if(functionId == PlayerFunctionType.Mounts.getType() 
				|| functionId == PlayerFunctionType.MilitaryRank.getType()
				|| functionId == PlayerFunctionType.GodWar.getType()
				) {
			tankBiz.updateTankAll(player);
		}
	}
	
	/**
	 * 坦克所有加成(坦克初始配表值+坦克自身属性加成+主公属性加成) 
	 *
	 * @author yanpeng 
	 * @param player
	 * @param tankId
	 * @return  
	 *
	 */
//	public TankAttr getTankAttrAll(Player player,int tankId){
//		TankAttr tankAttr = new TankAttr(); 
//		//主公属性加成
//		tankAttr.addAttr(getTankAttrPlayer(player));
//		//坦克初始配表值
//		tankAttr.addAttr(tankConfig.getTankSetting(tankId).getTankAttrInit());
//		//坦克自身属性加成
//		tankAttr.addAttr(getTankAttrSelf(player, tankId));
//		return tankAttr; 
//	}
	
	
	
	/**
	 * 坦克自身属性的加成
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public TankAttr getTankAttrSelf(Player player, Tank tank) {
		TankAttr tankAttr = new TankAttr();
		if(tank == null) return tankAttr;
		// 坦克配件加成
		tankAttr.addAttr(calTankReformAttr(player, tank));
		// 兽魂加成
		tankAttr.addAttr(calDriverAttr(player,tank));
		// 坦克羁绊加成
		tankAttr.addAttr(tankFettersBiz.calTankFriendAttr(player,tank));
		// 星级节点加成
		Map<TankAttrType, Double> starNodeAttr = calTankStarNodeAttr(tank);
		tankAttr.addAttr(starNodeAttr);
		// 兽魂对星级节点的加成
		tankAttr.addAttr(calTankDriverAddStarNodeAttr(tank, starNodeAttr));
		// 坦克勋章加成

		//玩家称号，额外加成
		TankAttr titleAttr = player.playerTitle().getTankAttr();
		tankAttr.addAttr(titleAttr);
		// 特工加成
//		tankAttr.addAttr(agentBiz.getAgentAddedByTankId(player, tank.getId()));
		//坦克专精
//		TankAttr specialAttr = tankSpecialBiz.getSpecialAttr(tank);
//		tankAttr.addAttr(specialAttr);
		//乘员加成
//		TankAttr passengerAttr = passengerBiz.getPassengerAttr(player,tank);
//		tankAttr.addAttr(passengerAttr);
		//魔改加成
//		TankAttr magicReformAttr = magicReformBiz.getMagicReformAttr(player,tank);
//		tankAttr.addAttr(magicReformAttr);
		//强化加成
//		TankAttr tankStrengthAttr = tankStrengthBiz.getStrengthAttr(player,tank);
//		tankAttr.addAttr(tankStrengthAttr);
		
		//坦克-车长-军职
//		tankAttr.addAttr(driverAdvanceBiz.getDriverAdAttr(player,tank));
//		tankAttr.addAttr(driverAdvanceBiz.getDriverAdFetterAttr(player,tank));
		//3s奇兵提供的属性
//		tankAttr.addAttr(tankSoldierBiz.getTankSoldierAddAttr(player,tank));
		
//		tankAttr.addAttr(tankJingzhuBiz.getJingzhuAllAttr(player, tank));
		//中控系统提供的属性
//		tankAttr.addAttr(controlBiz.getControlTankAttr(player,tank.getId()));
		
		return tankAttr;
	}
	
	public Map<Integer,TankAttr> getTankAttrPlayerByTankType(Player player){
		//-----------------全体战车加成----------------
		TankAttr tankAttr = new TankAttr();
		//指挥官座驾加成
		tankAttr.addAttr(calCarAttr(player));
		//指挥官军阶加成
		tankAttr.addAttr(calMilitaryAttr(player));
		//战鼓加成
//		tankAttr.addAttr(calSuperWeaponAttr(player));
		// 特工升级额外属性
//		tankAttr.addAttr(agentBiz.calAgentCenterAttr(player));
		//指挥官旗帜加成
		//指挥官指挥车加成 
		//指挥官装备加成 
		tankAttr.addAttr(calEquAttr(player));
		//总战力等级加成
		// 特工加成
//		tankAttr.addAttr(agentBiz.getAgentAdded(player));

		// 军阵
//		tankAttr.addAttr(militaryLineupBiz.getAttrMap(player));
		//指挥官--军工属性加成
//		tankAttr.addAttr(commanderBiz.getMilitaryProjectAttr(player));
		//纪念馆
//		tankAttr.addAttr(memorialHallAttrBiz.calHallAttrMap(player));
		//航母
//		tankAttr.addAttr(aircraftCarrierBiz.calAircraftCarrierAttr(player));
		//坦克成就
//		tankAttr.addAttr(tankStrengthBiz.getTaskAttr(player));
		//武器加成(只有类型为全体加成)
//		tankAttr.addAttr(guildFactoryBiz.getArmsAllAttr(player));
		//上阵战机加成
//		tankAttr.addAttr(aircraftCarrierBiz.getAirAttr(player));
		//图鉴大师
		tankAttr.addAttr(tankMasterBiz.getPlayerTankMasterAttr(player));
		//------------------按战车类型进行加成---------------------
		//类型加成
		Map<Integer,TankAttr> typeAdd = getTypeTankAttrAdd(player,tankAttr);
		
		warCraftBiz.initWarCraftLv(player, typeAdd);
		return typeAdd;
	}
	
	/**
	 * 获取最终坦克类型的加成
	 * @param player
	 * @param tankAttr 全体加成的属性
	 * @return
	 */
	public Map<Integer, TankAttr> getTypeTankAttrAdd(Player player,TankAttr tankAttr) {
		Map<Integer, TankAttr> addMap = Maps.newConcurrentMap();
		//专精
//		Map<Integer, TankAttr> masteryMap = masteryBiz.calAttr(player);
		//武器
//		Map<Integer,TankAttr> armsAttr = guildFactoryBiz.getArmsAttr(player);
//		Map<Integer, TankAttr> carrierEngineAttr = aircraftCarrierBiz.calAircraftCarrierEngineAttr(player);
		// 力量系统加成
//		Map<Integer,TankAttr> strengthAttr = strengthBiz.getStrengthAttr(player);
		//武器加成
		for(TankType type:TankType.values()){
			TankAttr attr = new TankAttr();
			//将全体加成的加到各种类型上
			attr.addAttr(tankAttr);
//			attr.addAttr(masteryMap.get(type.getType()));
//			attr.addAttr(armsAttr.get(type.getType()));
//			attr.addAttr(carrierEngineAttr.get(type.getType()));
//			attr.addAttr(strengthAttr.get(type.getType()));
			addMap.put(type.getType(), attr);
		}
		return addMap;
	}

	/**
	 * 玩家战力
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public long getPlayerCombat(Player player){
		//玩家技能战力
		long combat = this.calWarCraftCombat(player);
		
		//TODO:其他战力
		return combat;
	}
	
	/**
	 * 坦克进阶加成
	 *
	 * @author yanpeng 
	 * @param player
	 * @param tank  
	 *
	 */
	private Map<TankAttrType, Double> calTankReformAttr(Player player,Tank tank){
		//配件基础加成
		return tankConfig.getTankReformTemplate(tank).getReformAttrMap();
	}

	public Map<TankAttrType, Double> calTankStarNodeAttr(Tank tank){
		StarLevelTemplateImpl levelTemplate = tankConfig.getTankStarLevelTemplate(tank.getId(), tank.getStar(), tank.getStarNode());
		if (levelTemplate != null){
			return levelTemplate.getTotalAttrMap();
		}
		return Maps.newHashMap();
	}

	// 兽魂对星级节点的加成
	public Map<TankAttrType, Double> calTankDriverAddStarNodeAttr(Tank tank, Map<TankAttrType, Double> attrMap){
		Map<TankAttrType, Double> newAttrMap = Maps.newHashMap(attrMap);
		DriverTemplate driverTemplate = tankConfig.getDriverTemplate(tank.getId());
		// 兽魂觉醒加成
		float tecRate = driverTemplate.getEvolveTecRate(tank.getDriver().getEvolveLv());
		if (tecRate > 0){
			for (Map.Entry<TankAttrType, Double> entry : attrMap.entrySet()) {
				newAttrMap.put(entry.getKey(), entry.getValue() * tecRate);
			}
		}
		return newAttrMap;
	}


	/**
	 * 计算车长加成
	 *
	 * @author yanpeng 
	 * @param tank
	 * @return  
	 *
	 */
	private Map<TankAttrType, Double> calDriverAttr(Player player,Tank tank) {
		Map<TankAttrType, Double> attrMap = Maps.newHashMap();
		if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Driver)) {
			return attrMap;
		}
		int lv = tank.getDriver().getLv();
		DriverTemplate temp = tankConfig.getDriverTemplate(tank.getId());
		// 兽魂对初始值的加成
		float evolveAttrRate = temp.getEvolveAttrRate(tank.getDriver().getEvolveLv());
		Map<TankAttrType, Double> attrInit = tankConfig.getTankSetting(tank.getId()).getTankAttrInit();
		if (lv > 0) {
			attrMap.put(TankAttrType.ATK, (double) temp.getAtk_add() * lv + attrInit.get(TankAttrType.ATK) * evolveAttrRate);
			attrMap.put(TankAttrType.DEF, (double) temp.getDef_add() * lv + attrInit.get(TankAttrType.DEF) * evolveAttrRate);
			attrMap.put(TankAttrType.HP, (double) temp.getHp_add() * lv + attrInit.get(TankAttrType.HP) * evolveAttrRate);
			attrMap.put(TankAttrType.CRIT, Math.floor(temp.getCrit_add() * lv));
			attrMap.put(TankAttrType.CritDef, Math.floor(temp.getCrit_def_add() * lv));
		}
		Map<TankAttrType, Double> additionAttrMap = temp.getEvolveAdditionAttrMap(tank.getDriver().getEvolveLv());
		// 兽魂觉醒的额外属性
		if (additionAttrMap != null) {
			additionAttrMap.forEach((key, value) -> attrMap.merge(key, value, (x, y) -> (x + y)));
		}

		Map<Integer, Double> driverMap = Maps.newHashMap();
		attrMap.forEach((key, value) -> driverMap.put(key.getType(), value));
		tank.getDriver().setAttrMap(driverMap);
		return attrMap;
	}


	/**
	 * 计算座驾属性
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public Map<TankAttrType, Double> calCarAttr(Player player){
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Mounts)){
			return Maps.newHashMap(); 
		}
		int lv = player.playerCommander().getCarLv(); 
		CarTemplate temp = commanderConfig.getCarTemplate(lv);
		if(temp == null) {
			System.out.println("not found car lv:"+lv);
			return Maps.newHashMap();
		}
		int stage = temp.getCount() >0?player.playerCommander().getCarClickCount()/temp.getCount():0; 
		return temp.getAttrMap(stage);
	}
	
	/**
	 * 计算装备加成
	 *
	 * @author xjt 
	 * @param player
	 * @return  
	 *
	 */
	public Map<TankAttrType, Double> calEquAttr(Player player){
		Map<TankAttrType,Double> attrMap = Maps.newConcurrentMap();
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Equip)){
			return attrMap; 
		}
		for(int i = 1;i<=player.playerEquip().getEqus().length;i++){
			Equipment equ = player.playerEquip().getEquipment(i);
			//装备本身的属性
			Map<TankAttrType,Double> equAttrmap = equipmentConfig.getEquAttr(equ.getEquId());
			equAttrmap.forEach((key,value)->attrMap.merge(key, value, (x,y)->(x+y)));
			//强化的属性
			Map<TankAttrType,Double> strengthenAttrMap = equipmentConfig.getStrengthenAttr(equ.getId(),equ.getStrengthenLv());
			strengthenAttrMap.forEach((key,value)->attrMap.merge(key, value, (x,y)->(x+y)));
//			//精炼的属性(包含对装备的加成)
//			Map<TankAttrType,Double> refineAttrMap = equipmentConfig.getRefineAttr(equ.getId(),equ.getRefineLv());
//			refineAttrMap.forEach((key,value)->attrMap.merge(key, value, (x,y)->(x+y)));
			//石头的属性
			for (int stoneId : equ.getStone()) {
				if(stoneId > 0) {
					Map<TankAttrType,Double> stoneAttrMap = equipmentConfig.getStoneAttr(stoneId);
					stoneAttrMap.forEach((key,value)->attrMap.merge(key, value, (x,y)->(x+y)));
				}
			}
		}
		//光环属性
		Map<Integer,Integer> circleMap = player.playerEquip().getCircleMap();
		circleMap.forEach((type,lv)->{
			if(lv>0){
				PlayerArmCircleExtraTemplate circletemplate = equipmentConfig.getCircle(type, lv);
				circletemplate.getAttrMap().forEach((key,value)->attrMap.merge(key, value, (x,y)->(x+y)));
			}
		});
		//套装属性
//		List<Integer> suitIds = player.playerEquip().getSuitIds();
//		suitIds.forEach(id->{
//			PlayerArmSuitExtraTemplate template = equipmentConfig.getSuit(id);
//			template.getAttrMap().forEach((key,value)->attrMap.merge(key, value, (x,y)->(x+y)));
//		});
		return attrMap;
	}
	
	/**
	 * 计算指挥官军衔加成
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public Map<TankAttrType, Double> calMilitaryAttr(Player player){
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.MilitaryRank)){
			return Maps.newHashMap(); 
		}
		int lv = player.playerCommander().getMilitaryLv();
		MilitaryExtraTemplate temp = commanderConfig.getMilitaryExtraTemplate(lv);
		int stage = temp.getCount() >0 ? player.playerCommander().getMilitaryClickCount()/temp.getCount():0; 
		return temp.getAttrMap(stage);
	}

	
	/**
	 * 计算兵法的战斗力
	 * @author zxj 
	 * @return  
	 */
	private long calWarCraftCombat(Player player) {
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.WarCraft)){
			return 0;
		}
		long combat = 0;
		Map<Integer, Integer> skillMap = player.playerWarcraft().getSkillMap();
		for(int key: skillMap.keySet()) {
			WarCraftSkillTemplate warSkill = warCraftConfig.getWarCraftSkill(key);
			if(warSkill.getSkill_type()==1) {
				combat+=warSkill.getSkill_power()*skillMap.get(key);
			}
		}
		return combat;
	}

	//计算属性差值
	public static Map<Integer,Double> diffAttrMap(Map<TankAttrType, Double> oldMap,Map<TankAttrType, Double> newMap) {
		Map<Integer,Double> resultMap = Maps.newHashMap();
		for (Map.Entry<TankAttrType, Double> entry : newMap.entrySet()) {
			TankAttrType key = entry.getKey();
			double newVal = entry.getValue();
			double diffVal = newVal - oldMap.getOrDefault(key,0d);
			if(diffVal > 0) {
				resultMap.put(key.getType(),diffVal);
			}
		}
		return resultMap;
	}
	
	
	
}  
