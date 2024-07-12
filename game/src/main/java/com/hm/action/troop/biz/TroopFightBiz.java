package com.hm.action.troop.biz;

import com.google.common.collect.Lists;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.drop.PlayerDropObserver;
import com.hm.action.player.PlayerBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.spring.SpringUtil;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.CityTroop;
import com.hm.model.cityworld.Pvp1v1;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.cityworld.troop.ClonePlayerTroop;
import com.hm.model.cityworld.troop.NpcCityTroop;
import com.hm.model.cityworld.troop.PlayerCityTroop;
import com.hm.model.fight.FightProxy;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopItemContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.RandomUtils;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.AbstractPlayerFightTroop;
import com.hm.war.sg.troop.ClonePlayerFightTroop;
import com.hm.war.sg.troop.PlayerTroop;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class TroopFightBiz {
	public static boolean isCheckSameCampClose = false;

	@Resource
	private TroopBiz troopBiz;
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private PlayerDropObserver playerDropObserver;
	/**
	 * 部队战斗
	 * @param atkTroopId
	 * @param defTroopId
	 * @return
	 */
//	public WarResult worldCityFight(WorldCity worldCity,WorldTroop atkTroop,WorldTroop defTroop) {
//		
//		return null;
//	}
	
	/**
	 * 获取攻击方的战斗部队
	 * @param worldCity
	 * @return
	 */
	public AbstractFightTroop getAtkFightTroop(WorldCity worldCity) {
		WarResult warResult = worldCity.getCityFight().getWarResult();
		if(warResult != null) {
			//是否有上局战斗结果 有->取战斗胜利方
			AbstractFightTroop winTroop = warResult.getWinUnitGroup().getFightTroop();
			if(winTroop.isAtk()) {
				return winTroop;
			}
		}
		//从部队里找
		AbstractFightTroop fightTroop = getFristFightTroop(worldCity, worldCity.getAtkCityTroop());
		if(fightTroop != null) {
			fightTroop.loadFightData(true);
		}
		return fightTroop;
	}
	
	/**
	 * 获取防御方的战斗部队
	 * @param worldCity
	 * @return
	 */
	public AbstractFightTroop getDefFightTroop(WorldCity worldCity) {
		WarResult warResult = worldCity.getCityFight().getWarResult();
		if(warResult != null) {
			//是否有上局战斗结果 有->取战斗胜利方
			AbstractFightTroop winTroop = warResult.getWinUnitGroup().getFightTroop();
			if(!winTroop.isAtk()) {
				return winTroop;
			}
		}
		//从部队里找
		AbstractFightTroop fightTroop = getFristFightTroop(worldCity, worldCity.getDefCityTroop());
		if(fightTroop != null) {
			fightTroop.loadFightData(false);
		}
		return fightTroop;
	}
	
	private AbstractFightTroop getFristFightTroop(WorldCity worldCity,CityTroop cityTroop) {
		TroopItemContainer troopContainer = TroopServerContainer.of(worldCity);
		for (BaseCityFightTroop temp : Lists.newArrayList(cityTroop.getTroopList())) {
			if(temp.getTroopType() == CityTroopType.PlayerTroop.getType()) {
				WorldTroop worldTroop = troopContainer.getWorldTroop(temp.getId());
				if(worldTroop == null) {
					cityTroop.removeTroop(temp.getId());
					worldCity.saveDB();
					continue;
				}
			}
			AbstractFightTroop troop = buildFightTroop(worldCity,temp);
			if(troop != null) {
				return troop;
			}
		}
		return null;
	}
	
	/**
	 * 战斗结束后同步胜利方的战斗血量
	 * @param worldCity
	 * @param result
	 */
	public void doWarResultEndForSyncTroopHp(WorldCity worldCity,WarResult result) {
		if(result.isAtkWin()) {
			AbstractFightTroop winTroop = result.getAtk().getFightTroop();
			winTroop.doWarResult(result.getAtk());
			syncTankHpForWorldTroop(worldCity, winTroop);
		}else{
			AbstractFightTroop winTroop = result.getDef().getFightTroop();
			winTroop.doWarResult(result.getDef());
			syncTankHpForWorldTroop(worldCity, winTroop);
		}
	}
	
	/**
	 * 保存世界部队血量，通知玩家
	 * @param worldCity
	 * @param fightTroop
	 */
	private void syncTankHpForWorldTroop(WorldCity worldCity,AbstractFightTroop fightTroop) {
		if(!(fightTroop instanceof PlayerTroop)) {
			return;
		}
		PlayerTroop playerTroop = (PlayerTroop)fightTroop;
		TroopItemContainer troopContainer = TroopServerContainer.of(worldCity);
		WorldTroop worldTroop = troopContainer.getWorldTroop(playerTroop.getId());
		if(worldTroop == null) {
			return;
		}
		//worldTroop里的ArmyList和playerTroop中的tankList是一个对象
		worldTroop.getTroopArmy().SetChanged();
		worldTroop.saveDB();
		//同步血量
		troopBiz.sendWorldTroopUpdate(worldTroop);
	}
	
	/**
	 * 处理部队死亡
	 * @param worldCity
	 * @param loseTroop
	 */
	public void doTroopDeath(WorldCity worldCity,AbstractFightTroop loseTroop) {
		boolean isDel = worldCity.removeTroop(loseTroop.getId());
		//清空死亡的保留状态
		loseTroop.clearRetainState();
		if(loseTroop.isNpc()) {
			return;
		}
		if(loseTroop.isClonePlayer()) {
			//抛出镜像死亡事件
			ObserverRouter.getInstance().notifyObservers(ObservableEnum.CloneTroopDeath, null, loseTroop);
			return;
		}
		if(!isDel) {
			log.error(loseTroop.getId()+"删除失败:死亡删除部队出错:"+worldCity.getId());
		}
		PlayerTroop playerTroop = (PlayerTroop)loseTroop;
		//改变状态
		troopBiz.changeCityTroopState(worldCity, new PlayerCityTroop(playerTroop.getId()), TroopState.Death);
	}
	
	/**
	 * 检查城市内的部队状态
	 * @param worldCity
	 */
	public void checkWorldCityTroopState(WorldCity worldCity) {
        boolean hasFight = worldCity.getAtkCityTroop().getTroopList()
                .stream().anyMatch(e -> getCityFightTroopState(worldCity, e) != TroopState.PvpOneByOne.getType());
        if (!hasFight) {
			//过滤pvp的部队
			List<BaseCityFightTroop> troopList = worldCity.getDefCityTroop().getTroopList()
					.stream().filter(e -> getCityFightTroopState(worldCity, e) != TroopState.PvpOneByOne.getType())
					.collect(Collectors.toList());
			troopBiz.changeCityTroopState(worldCity, troopList, TroopState.None);
			return;
		}
		for (AtkDefType type : Lists.newArrayList(AtkDefType.ATK,AtkDefType.DEF)) {
			//前3排的部队设置为战斗状态,剩余为等待
			List<BaseCityFightTroop> troopList = worldCity.getCityTroop(type.getType()).getTroopList()
					.stream().filter(e -> getCityFightTroopState(worldCity, e) != TroopState.PvpOneByOne.getType())
					.collect(Collectors.toList());
			for (int i = 0; i < troopList.size(); i++) {
				BaseCityFightTroop fightTroop = troopList.get(i);
				TroopState newState = i < GameConstants.LockTroopNum?TroopState.Fight:TroopState.FightWait;
				troopBiz.changeCityTroopState(worldCity, fightTroop, newState);
			}
		}
	}
	/**
	 * 获取当前战斗部队状态
	 * @param worldCity
	 * @param troop
	 * @return
	 */
	public int getCityFightTroopState(WorldCity worldCity,BaseCityFightTroop troop) {
		return troop.getCityTroopState(worldCity);
	}
	private AbstractFightTroop createFightTroop(WorldCity worldCity,BaseCityFightTroop troop) {
		TroopItemContainer troopContainer = TroopServerContainer.of(worldCity);
		if(troop.getTroopType() == CityTroopType.NpcTroop.getType()) {
			NpcCityTroop npcTroop = (NpcCityTroop)troop;
			if(npcTroop.getState() != TroopState.PvpOneByOne.getType()) {
				return npcTroop.createNpcTroop();//不在单挑中的npc
			}
		}else if(troop.getTroopType() == CityTroopType.PlayerTroop.getType()){
			WorldTroop worldTroop = troopContainer.getWorldTroop(troop.getId());
			if(worldTroop != null) {
				//检查单挑
				if(worldTroop.getState() == TroopState.PvpOneByOne.getType() 
						&& worldCity.getCityFight().pvpListIsEmpty()) {
					worldTroop.changeState(TroopState.Fight);
				}
				if(worldTroop.getState() != TroopState.PvpOneByOne.getType()) {
					return worldTroop.buildPlayerTroop();
				}
			}
		}else if(troop.getTroopType() == CityTroopType.ClonePlayer.getType()){
			ClonePlayerTroop clonePlayerTroop = (ClonePlayerTroop)troop;
			//检查单挑
			if(clonePlayerTroop.getState() == TroopState.PvpOneByOne.getType() 
					&& worldCity.getCityFight().pvpListIsEmpty()) {
				clonePlayerTroop.changeState(TroopState.Fight);
			}
			if(clonePlayerTroop.getState() != TroopState.PvpOneByOne.getType()) {
				return new ClonePlayerFightTroop(clonePlayerTroop);//不在单挑中的npc
			}
		}
		return null;
	}
	
	public AbstractFightTroop buildFightTroop(WorldCity worldCity,BaseCityFightTroop troop) {
		AbstractFightTroop fightTroop = createFightTroop(worldCity, troop);
		if(fightTroop != null) {
			int moraleMax = troop.getMoraleMax();
			if(troop.isNpcTroop() && moraleMax <= 0) {
				troop.setMoraleMax(SpringUtil.getBean(CommValueConfig.class).getCommValue(CommonValueType.MoraleMax));
			}
			fightTroop.setMoraleMax(troop.getMoraleMax());
		}
		return fightTroop;
	}
	
	
	/**
	 * 检查玩家的部队是否异常
	 * @param player
	 */
	public void checkTroopStateError(Player player) {
		TroopServerContainer.of(player).getWorldTroopByPlayer(player)
			.forEach(e -> checkTroopStateError(e));
	}
	/**
	 * 检查部队状态是否错误
	 * @param worldTroop
	 */
	public void checkTroopStateError(WorldTroop worldTroop) {
		int troopState = worldTroop.getState();
		if(troopState == TroopState.Fight.getType() || troopState == TroopState.FightWait.getType()) {
			if(worldTroop.getCityId() == GameConstants.PeaceId) {
				worldTroop.changeState(TroopState.None);
				worldTroop.saveDB();
				log.error(worldTroop.getServerId()+"_"+worldTroop.getId()+"部队状态异常错误!!");
				return;
			}
			WorldCity worldCity = WorldServerContainer.of(worldTroop).getWorldCity(worldTroop.getCityId());
			if(worldCity == null || !worldCity.hasFight()) {
				worldTroop.changeState(TroopState.None);
				worldTroop.saveDB();
				log.error(worldTroop.getServerId()+"_"+worldTroop.getId()+"部队状态异常错误!!");
				return;
			}
		}
	}
	
	//#cmd:clonetroop#4
	public boolean clonePlayerTroop(Player player,String troopId) {
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null || worldTroop.getState() == TroopState.Death.getType()) {
			player.sendErrorMsg(SysConstant.WorldTroop_TroopDeath_Clone);
			return false;
		}
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
		if(worldCity == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_CityNot_Clone);
			return false;
		}
		boolean hasFight = worldCity.hasFight();
		if(!hasFight) {
			player.sendErrorMsg(SysConstant.WorldTroop_CityNot_Clone);
			return false;
		}
		AtkDefType atkDefType = worldCity.getAtkDefType(troopId);
		if(atkDefType == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_CityNot_Clone);
			return false;
		}
		boolean isFriend = atkDefType == AtkDefType.DEF;
		ClonePlayerTroop clonePlayerTroop = new ClonePlayerTroop(worldTroop);
		if(isFriend) {
			if(hasFight) {
				TroopState troopState = worldBiz.checkTroopIsFightState(worldCity, AtkDefType.DEF)?TroopState.Fight:TroopState.FightWait;
				clonePlayerTroop.changeState(troopState);
			}
			worldCity.getDefCityTroop().addTroop(clonePlayerTroop);
		}else{
			TroopState troopState = worldBiz.checkTroopIsFightState(worldCity, AtkDefType.ATK)?TroopState.Fight:TroopState.FightWait;
			clonePlayerTroop.changeState(troopState);
			worldCity.getAtkCityTroop().addTroop(clonePlayerTroop);
		}
		worldCity.saveDB();
		
		//add clone troop
		player.playerCloneTroops().addCityTroop(worldCity);
		
		//广播进入城市
		AtkDefType atkType = isFriend ? AtkDefType.DEF : AtkDefType.ATK;
		worldBiz.broadPlayerIntoCity(player, worldTroop ,worldCity, atkType,true);
		
		//广播城市部队变化
		worldBiz.broadWorldCityTroopChange(worldCity);
		return true;
	}
	
	
	public int doPvp1v1(WorldCity worldCity,Player player,AtkDefType type,List<String> canPvpList) {
		AtkDefType otherType = type == AtkDefType.ATK ? AtkDefType.DEF:AtkDefType.ATK;
		//倒着找可单挑的部队
		BaseCityFightTroop playerTroop = findWaitTroopFor1v1(worldCity, type, canPvpList);
		if(playerTroop == null) {
			return 0;
		}
		BaseCityFightTroop targetTroop = findWaitTroopFor1v1ForEnemy(worldCity, otherType);
		if(targetTroop == null) {
			return 0;//没有对方
		}
		AbstractFightTroop atkTroop = buildFightTroop(worldCity, playerTroop);
		if(atkTroop == null) {
			return 0;
		}
		AbstractFightTroop defTroop = buildFightTroop(worldCity, targetTroop);
		if(defTroop == null) {
			return 0;
		}
		player.playerTemp().updatePvpTime();//更新单挑时间
		boolean atkIsSameGuild = player.getGuildId() == worldCity.getBelongGuildId();
		loadTroopFightInfo(worldCity, atkTroop, FightType.Pvp, true,atkIsSameGuild);
		loadTroopFightInfo(worldCity, defTroop, FightType.Pvp, false,!atkIsSameGuild);
		//状态改变
		troopBiz.changeCityTroopState(worldCity, Lists.newArrayList(playerTroop,targetTroop), TroopState.PvpOneByOne);

		WarResult warResult = new FightProxy().doFight(atkTroop, defTroop);
		worldCity.getCityFight().addPvp1v1(new Pvp1v1(warResult));
		//计算胜利玩家掉落
		playerDropObserver.calWarResultPlayerWinDrop(worldCity,warResult);
		//記錄士氣
        playerTroop.reduceMorale(atkTroop);
        targetTroop.reduceMorale(defTroop);
		player.notifyObservers(ObservableEnum.PvpOneByOneLaunch);
		
		//发送消耗石油
		player.sendMsg(MessageComm.S2C_Troop_PvpOneByOne);//成功
		player.sendUserUpdateMsg(true);//延迟保存库
		return 2;
	}
	
	//找出我自己的部队
	private BaseCityFightTroop findWaitTroopFor1v1(WorldCity worldCity,AtkDefType type,List<String> filterList) {
		CityTroop cityTroop = worldCity.getCityTroop(type.getType());
		return filterList.stream().map(troopId -> cityTroop.getCityFightTroop(troopId))
				.filter(Objects::nonNull)
				.findFirst().orElse(null);
	}
	
	//找出单挑敌人部队
	private BaseCityFightTroop findWaitTroopFor1v1ForEnemy(WorldCity worldCity,AtkDefType type) {
		TroopItemContainer troopItemContainer = TroopServerContainer.of(worldCity);
		List<BaseCityFightTroop> mainTroops = Lists.newArrayList(worldCity.getCityTroop(type.getType()).getTroopList());
		List<BaseCityFightTroop> luckList = Lists.newArrayList();
		for (int i = mainTroops.size()-1; i >= 0; i--) {
			if(i < GameConstants.LockTroopNum) {
				break;
			}
			BaseCityFightTroop temp = mainTroops.get(i);
			if(temp.getTroopType() == CityTroopType.PlayerTroop.getType()) {
				WorldTroop worldTroop = troopItemContainer.getWorldTroop(temp.getId());
				if(worldTroop != null && worldTroop.getState() == TroopState.FightWait.getType()) {
					luckList.add(temp);
				}
			}else if(temp.getTroopType() == CityTroopType.NpcTroop.getType()){
				if(((NpcCityTroop)temp).getState() == TroopState.FightWait.getType()) {
					luckList.add(temp);
				}
			}else if(temp.getTroopType() == CityTroopType.ClonePlayer.getType()){
				if(((ClonePlayerTroop)temp).getState() == TroopState.FightWait.getType()) {
					luckList.add(temp);
				}
			}
		}
		if(luckList.isEmpty()) {
			return null;
		}
		return RandomUtils.randomEle(luckList);
	}
	
	/**
	 * 检查攻击者是否和城池是一方阵营
	 * @param worldCity
	 * @param atkFightTroop
	 * @return
	 */
	public boolean checkAtkTroopForCitySameGuild(WorldCity worldCity, AbstractFightTroop atkFightTroop) {
		if(isCheckSameCampClose) {
			return false;
		}
		if(atkFightTroop == null) {
			return true;
		}
		if(!(atkFightTroop instanceof AbstractPlayerFightTroop)) {
			return false;//只检查玩家
		}
		AbstractPlayerFightTroop atkTroop = (AbstractPlayerFightTroop)atkFightTroop;
		Player player = atkTroop.getPlayer();
		if(player == null) {
			return worldCity.getAtkCityTroop().removeTroop(atkFightTroop.getId());
		}
		if(player.getGuildId() == worldCity.getBelongGuildId()) {
			//同一阵营
			BaseCityFightTroop luckTroop = worldCity.getAtkCityTroop().getCityFightTroop(atkFightTroop.getId());
			if(luckTroop != null) {
				worldCity.getAtkCityTroop().removeTroop(atkFightTroop.getId());
				worldCity.getDefCityTroop().addTroop(luckTroop);
				if(worldCity.getAtkCityTroop().getTroopSize() <= 0) {
					worldBiz.doWorldCityDefWin(worldCity);
				}
				return true;
			}
		}
		return false;
	}
	
//	public void loadDefTroopForCity(WorldCity worldCity,AbstractFightTroop fightTroop) {
//		if(worldCity == null || WorldType.getTypeByCityId(worldCity.getId()) != WorldType.Normal) {
//			return;
//		}
//		int campId = worldCity.getCamp();
//		if(campId > 0) {
//			CampContainer campContainer = SpringUtil.getBean(CampContainer.class);
//			Camp camp = campContainer.getItemContainer(worldCity).getCamp(campId);
//			if(camp != null && camp.getCampArea().isProtectMainCity(worldCity.getId())) {
//				this.extraSkillLvs.put(WarComm.ProtectCitySkillId, 1);
//			}
//		}
//	}
	
	/**
	 * 加载战斗
	 * @param worldCity
	 * @param fightTroop
	 * @param fightType
	 * @param isAtk
	 */
	public void loadTroopFightInfo(WorldCity worldCity,AbstractFightTroop fightTroop,FightType fightType,boolean isAtk,boolean isSameGuild) {
		fightTroop.loadFightData(isAtk);
		fightTroop.setSameGuild(isSameGuild);
		fightTroop.loadFightParm(fightType, worldCity.getId());
	}
	
}
