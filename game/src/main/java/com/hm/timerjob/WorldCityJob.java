package com.hm.timerjob;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.cityworld.biz.WorldCityBiz;
import com.hm.action.drop.PlayerDropObserver;
import com.hm.action.guildwar.biz.WarRewardBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.action.troop.biz.TroopFightBiz;
import com.hm.config.GameConstants;
import com.hm.enums.*;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.Pvp1v1;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.fight.FightProxy;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class WorldCityJob {
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private TroopFightBiz troopFightBiz;
	@Resource
	private WarRewardBiz warRewardBiz;
	@Resource
	private PlayerDropObserver playerDropObserver;
	@Resource
	private WorldCityBiz worldCityBiz;
	
	

	private List<WorldCity> getCheckWorldCitys(int serverId) {
		return WorldServerContainer.of(serverId).getWorldCitys(WorldType.Normal);
	}
	
	public void doJob(int serverId) {
		int status = GuildWarUtils.getServerWarStatus(serverId);
		if(status == GuildWarStatus.WaitCal.getType()) {
			//清除状态
			GuildWarUtils.removeServerWarStatus(serverId);
			worldCityBiz.clearWorldCity(serverId);
			ObserverRouter.notifyObservers(ObservableEnum.GuildWarEnd,serverId);
			return;
		}
		if(status != GuildWarStatus.Running.getType()) {
			return;
		}
		for (WorldCity worldCity : getCheckWorldCitys(serverId)) {
			try {
				worldCity.lockWrite();
				//执行城市内归属战斗
				doJobWorldCity(worldCity);
				//单挑战斗
				doJobWorldCityPvp1v1(worldCity);
			} catch (Exception e) {
				log.error(worldCity.getServerId()+"_"+worldCity.getId()+"城池战斗出错", e);
			} finally {
				worldCity.unlockWrite();
			}
		}
	}
	
	public void doJobWorldCity(WorldCity worldCity) {
		if(!worldCity.hasFight()) {
			//检查npc生成
			worldBiz.checkWorldCityNpcCreate(worldCity);
			return;
		}
		if(worldCity.getCityFight().isFighting()) {
			return;
		}
		WarResult warResult = worldCity.getCityFight().getWarResult();
		if(warResult != null) {
			//计算胜者剩余血量
			troopFightBiz.doWarResultEndForSyncTroopHp(worldCity, warResult);
			//把败者删除,变成死亡状态
			AbstractFightTroop loseTroop = warResult.getLoseUnitGroup().getFightTroop();
			troopFightBiz.doTroopDeath(worldCity, loseTroop);
//			log.error("战斗死亡:"+loseTroop.getId()+"  =="+worldCity.hasTroop(loseTroop.getId()));
			worldCity.saveDB();
			warResult.calWinNum();
			//计算战斗杀敌和死亡奖励
			warRewardBiz.doGuildWarResult(worldCity, warResult);
		}
		AbstractFightTroop atkFightTroop = troopFightBiz.getAtkFightTroop(worldCity);
		if(atkFightTroop == null) {//防守方胜利了
			if(warResult != null) {//刚打完
				worldBiz.doWorldCityDefWin(worldCity);
			}
			return;
		}
		AbstractFightTroop defFightTroop = troopFightBiz.getDefFightTroop(worldCity);
		if(defFightTroop == null) {
			//攻击方胜利了
			worldBiz.doWorldCityAtkWin(worldCity,atkFightTroop);
			return;
		}
		int atkSize = worldCity.getTroopSize(AtkDefType.ATK);//攻击方部队数量
		//服务器战斗异常校验
		if(atkSize == 0) {
			worldCity.getCityFight().clearFightData();
			return;
		}
		if(!checkWorldFight(worldCity, atkFightTroop, defFightTroop)) {
			worldCity.getCityFight().clearFightData();
			return;
		}
//		if(troopFightBiz.checkAtkTroopForCitySameGuild(worldCity, atkFightTroop)) {
//			log.error(worldCity.getId()+"攻击者和城池阵营相同");
//			return;
//		}
		//========================开始战斗==============================
		//检查城市内部队状态
		troopFightBiz.checkWorldCityTroopState(worldCity);
		//部队重置为战斗状态
		troopFightBiz.loadTroopFightInfo(worldCity, atkFightTroop, FightType.AtkCity, true,false);
		troopFightBiz.loadTroopFightInfo(worldCity, defFightTroop, FightType.DefCity, false,true);

		WarResult newWarResult = new FightProxy().doFight(atkFightTroop, defFightTroop);

		//计算胜利玩家掉落
		playerDropObserver.calWarResultPlayerWinDrop(worldCity,newWarResult);
		
		newWarResult.syncWinNum(warResult);//同步胜利次数
		worldCity.getCityFight().setWarResult(newWarResult);
		//广播给所有城市的人
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldCityFightStart);
		serverMsg.addProperty("warResult", newWarResult);
		serverMsg.addProperty("cityId", worldCity.getId());
		serverMsg.addProperty("startFrame", 0);
		serverMsg.addProperty("atkSize", atkSize);
		serverMsg.addProperty("defSize", worldCity.getTroopSize(AtkDefType.DEF));
		worldBiz.broadWorldCityMsg(worldCity, serverMsg);
		
		BaseCityFightTroop atkTroop = worldCity.getAtkCityTroop().getCityFightTroop(atkFightTroop.getId());
		if (atkTroop != null) atkTroop.reduceMorale(atkFightTroop);
		BaseCityFightTroop defTroop = worldCity.getDefCityTroop().getCityFightTroop(defFightTroop.getId());
		if (defTroop != null) defTroop.reduceMorale(defFightTroop);
	}
	
	//判断战斗是否合法
	private boolean checkWorldFight(WorldCity worldCity,AbstractFightTroop atkFightTroop,AbstractFightTroop defFightTroop) {
		try {
			if(worldCity.getCityFight().getWarResult() == null) {
				return true;
			}
			BaseCityFightTroop firstAtk = worldCity.getAtkCityTroop().getFirstFightTroop();
			if(firstAtk == null || !StrUtil.equals(firstAtk.getId(), atkFightTroop.getId())) {
				return false;
			}
//			BaseCityFightTroop firstdef = worldCity.getDefCityTroop().getFirstFightTroop();
//			if(firstdef == null || !StrUtil.equals(firstdef.getId(), defFightTroop.getId())) {
//				return false;
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private void doJobWorldCityPvp1v1(WorldCity worldCity) {
		List<Pvp1v1> pvpList = worldCity.getCityFight().getPvp1v1List();
		if(CollUtil.isEmpty(pvpList)) {
			return;
		}
		for (int i = pvpList.size()-1; i >= 0; i--) {
			try {
				Pvp1v1 pvp1v1 = pvpList.get(i);
				if(pvp1v1 == null) {
					pvpList.remove(i);
					continue;
				}
				WarResult warResult = pvp1v1.getWarResult();
				if(!warResult.isOver()) {
					continue;
				}
//				removeList.add(pvp1v1);//单挑结束
				pvpList.remove(i);
				AbstractFightTroop atk = warResult.getAtk().getFightTroop();
				AbstractFightTroop def = warResult.getDef().getFightTroop();
				AbstractFightTroop win = warResult.isAtkWin()?atk:def;
				AbstractFightTroop lose = warResult.isAtkWin()?def:atk;
				
				BaseCityFightTroop winTroop = worldCity.getCityFightTroop(win.getId());
				if(winTroop != null) {
					//同步血量
					troopFightBiz.doWarResultEndForSyncTroopHp(worldCity, warResult);
					troopBiz.changeCityTroopState(worldCity, winTroop, checkPvpWinTroopState(win, worldCity));
				}
				//失败者死亡
				troopFightBiz.doTroopDeath(worldCity, lose);
				log.error("单挑死亡:"+lose.getId()+"  =="+worldCity.hasTroop(lose.getId()));
				//计算功勋奖励
				warRewardBiz.doPvp1v1WarResult(worldCity, warResult, win, lose);
				//广播城市部队变化
				worldBiz.broadWorldCityTroopChange(worldCity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		if(CollUtil.isNotEmpty(removeList)) {
//			pvpList.removeAll(removeList);
//		}
	}
	
	private TroopState checkPvpWinTroopState(AbstractFightTroop troop,WorldCity worldCity) {
		AtkDefType winType = worldCity.getAtkDefType(troop.getId());
		if(winType == null) {
			return TroopState.None;
		}
		boolean hasFight = worldCity.hasFight();
		if(hasFight) {
			int troopIndex = worldCity.getCityTroop(winType.getType()).getTroopIndex(troop.getId());
			return troopIndex < GameConstants.LockTroopNum?TroopState.Fight:TroopState.FightWait;
		}
		//当前无战斗 
		if(winType == AtkDefType.DEF) {
			return TroopState.None;//防御方
		}
		return TroopState.Fight;//防御方
	}
}
