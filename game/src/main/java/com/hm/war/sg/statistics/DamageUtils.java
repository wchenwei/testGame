package com.hm.war.sg.statistics;

import com.hm.war.sg.unit.Unit;

import java.util.Objects;

public class DamageUtils {
	public static boolean isClose = false;
	public static DamageItem damageItem;
	
	public static void resetData() {
		if(isClose) {
			return;
		}
		damageItem = new DamageItem();
	}
	
	public static void addDamage(Unit unit, int atkId, long skillId, long damage) {
		if(isClose) {
			return;
		}
		Unit atkUnit = unit.getDefGroup().getUnits().stream().filter(Objects::nonNull)
				.filter(e -> e.getId() == atkId).findFirst().orElse(null);
		if(atkUnit != null) {
			damageItem.addDamage(atkUnit, skillId, damage);
		}else{
//			System.err.println(atkId+"=="+skillId+"=="+unit.getId()+"="+damage);
			if(atkId == -1 || atkId == -2) {
				damageItem.addExtraHurt(atkId, damage);
			}
		}
	}
	
	public static String damageInfo() {
		if(damageItem == null) {
			return "无战斗";
		}
		return damageItem.damageInfo();
	}
	
}
