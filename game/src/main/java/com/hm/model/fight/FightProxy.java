package com.hm.model.fight;

import com.google.common.collect.Lists;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.war.sg.FightMode;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.WarParam;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.PlayerTroop;
import com.hm.war.sg.troop.TankArmy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class FightProxy {
	public WarResult doFight(WorldTroop atkTroop,WorldTroop defTroop) {
		return doFight(atkTroop.buildPlayerTroop(), defTroop.buildPlayerTroop());
	}
	
	public WarResult doFight(AbstractFightTroop atkTroop,AbstractFightTroop defTroop) {
		return doFight(atkTroop, defTroop,new WarParam());
	}
	/**
	 * 战斗
	 * @param atkTroop
	 * @param defTroop
	 * @return
	 */
	public WarResult doFight(AbstractFightTroop atkTroop, AbstractFightTroop defTroop, WarParam warParam) {
		try {
			UnitGroup atkGroup = atkTroop.createUnitGroup();
			UnitGroup defGroup = defTroop.createUnitGroup();
			atkGroup.setFightTroop(atkTroop);
			defGroup.setFightTroop(defTroop);
			//设置进攻方和防守方
			atkGroup.loadId(true);
			defGroup.loadId(false);
			atkGroup.setWarParam(warParam);
			defGroup.setWarParam(warParam);
			//战斗
			WarResult result = new FightMode().fight(atkGroup, defGroup,warParam.getMaxFrame());
			//计算战斗开始，结束时间
			result.calFightTime(warParam.getFrameTime());
//			if(result.isAtkWin()) {
//				atkTroop.doWarResult(atkGroup);
//			}else{
//				defTroop.doWarResult(defGroup);
//			}
			atkTroop.clearTroopFightInfo();
			defTroop.clearTroopFightInfo();
			return result;
		} catch (Exception e2) {
			log.error("城战出错", e2);
		}
		return null;
	}
	
	public static AbstractFightTroop createPlayer(long playerId) {
		PlayerTroop playerTroop = new PlayerTroop(playerId,"1");
		List<TankArmy> tankList = Lists.newArrayList();
		tankList.add(new TankArmy(0, 3));
		tankList.add(new TankArmy(1, 4));
		tankList.add(new TankArmy(2, 20));
		tankList.add(new TankArmy(6, 6));
		tankList.add(new TankArmy(7, 14));
		playerTroop.setTankList(tankList);
		return playerTroop;
	}
	
	public void test() {
//		SettingManager.getInstance().init();
//		NpcConfig npcConfig = SpringUtil.getBean(NpcConfig.class);
//		NpcTroopTemplate template = npcConfig.getNpcTroopTemplate(100101);
//		WarResult result = new FightProxy().doFight(createPlayer(600015), new NpcTroop(template));
//		System.err.println("======================");
//		outLastHp(result.getAtk());
//		System.err.println("======================");
//		outLastHp(result.getDef());
//		System.err.println(result.isAtkWin());
	}
	
}
