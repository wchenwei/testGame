package com.hm.war.sg.statistics;

import com.google.common.collect.Maps;
import com.hm.war.sg.unit.Unit;
import lombok.Data;

import java.util.Map;

@Data
public class DamageItem {
	private Map<Integer,UnitDamage> unitMap = Maps.newConcurrentMap();
	private Map<Integer,UnitDamage> defMap = Maps.newConcurrentMap();
	
	private long atk;
	private long def;
	
	public void addDamage(Unit unit, long skillId, long damage) {
		int tankId = unit.getSetting().getTankId();
		Map<Integer,UnitDamage> unitMap = getMap(unit);
		UnitDamage temp = unitMap.get(tankId);
		if(temp == null) {
			temp = new UnitDamage(tankId);
			unitMap.put(tankId, temp);
		}
		temp.addDamage(skillId, damage);
	}
	
	public void addExtraHurt(int atkId,long damage) {
		if(atkId == -2) {
			this.def += damage;
		}else{
			this.atk += damage;
		}
	}
	
	public String damageInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("攻击座驾:"+atk+"\r\n");
		for (UnitDamage unitDamage : unitMap.values()) {
			sb.append(unitDamage.toDamageInfo()+"\r\n");
		}
		sb.append("==================================\r\n");
		sb.append("防御座驾:"+def+"\r\n");
		for (UnitDamage unitDamage : defMap.values()) {
			sb.append(unitDamage.toDamageInfo()+"\r\n");
		}
		return sb.toString();
	}
	
	public Map<Integer,UnitDamage> getMap(Unit unit) {
		if(unit.getId() > 9) {
			return defMap;
		}else{
			return unitMap;
		}
	}
}
