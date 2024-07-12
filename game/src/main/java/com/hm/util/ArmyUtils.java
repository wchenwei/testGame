package com.hm.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.NpcTroop;
import com.hm.war.sg.troop.TankArmy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArmyUtils {
	
	public static String getGuildKillStr(Map<Integer,Integer> guildKillMap){
		if(guildKillMap == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int key : guildKillMap.keySet()) {
			sb.append(key+":"+guildKillMap.get(key)+",");
		}
		return StringUtil.subEndDouhao(sb.toString());
	}
	
	public static Map<Integer,Integer> strToGuildKillMap(String killStr) {
		Map<Integer,Integer> guildKillMap = Maps.newConcurrentMap();
		if(killStr == null || killStr.isEmpty()) {
			return guildKillMap;
		}
		for (String temp : killStr.split(",")) {
			String[] t = temp.split(":");
			guildKillMap.put(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
		}
		return guildKillMap;
	}
	
	public static List<TankArmy> createTankArmys(String armys) {
		ArrayList<TankArmy> tankList = Lists.newArrayList();
		for (String str : armys.split(",")) {
			TankArmy tankArmy = new TankArmy(Integer.parseInt(str.split(":")[0]), Integer.parseInt(str.split(":")[1]));
			tankList.add(tankArmy);
		}
		return tankList;
	}
	
	public static List<TankArmy> createFullTankArmys(String armys) {
		ArrayList<TankArmy> tankList = Lists.newArrayList();
		for (String str : armys.split(",")) {
			TankArmy tankArmy = new TankArmy(Integer.parseInt(str.split(":")[0]), Integer.parseInt(str.split(":")[1]));
			tankArmy.setFullHp();
			tankList.add(tankArmy);
		}
		return tankList;
	}
	
	public static String buildUnitStr(AbstractFightTroop troop) {
		StringBuilder sb = new StringBuilder();
		for (TankArmy tankArmy : troop.getTankList()) {
			sb.append(tankArmy.getIndex()+":"+tankArmy.getId()+":"+tankArmy.getHp());
			sb.append(",");
		}
		return StringUtil.subEndDouhao(sb.toString());
	}
	
	public static AbstractFightTroop createDefTroop(String defArms) {
		List<TankArmy> armyList = createTankArmysAndHp(defArms);
		NpcTroop npcTroop = new NpcTroop("npc");
		npcTroop.loadNpcInfo(armyList, 1, "", 1, 1, 1, 1);
		return npcTroop;
	}
	
	/**
	 * 创建tankArmys  含血量
	 * @param armys
	 * @return
	 */
	public static List<TankArmy> createTankArmysAndHp(String armys) {
		ArrayList<TankArmy> tankList = Lists.newArrayList();
		for (String str : armys.split(",")) {
			TankArmy tankArmy = new TankArmy(Integer.parseInt(str.split(":")[0]), Integer.parseInt(str.split(":")[1]));
			int hp = Integer.parseInt(str.split(":")[2]);
			if(hp < 0) {
				tankArmy.setFullHp();
			}else{
				tankArmy.setHp(hp);
			}
			tankList.add(tankArmy);
		}
		return tankList;
	}
}
