/**
 * 
 */
package com.hm.enums;

/**
 * Title: WarUnitType.java
 * Description:战斗单位类型 用于标记附件属性
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2015年4月30日 上午10:36:17
 * @version 1.0
 */
public enum WarType {
	//副本，扫荡，流寇，国战，城战
	Mission(20,false,false,false,"副本"),
	MissionTimes(21,false,false,false,"副本扫荡"),
	Bandits(22,true,true,false,"流寇"),
	ContryWar(23,true,true,true,"国战"),
	CityWar(24,true,true,true,"城战"),
	WorldBoss(25,false,false,false,"世界boss"),
	ATKGATHER(26,true,true,false,"攻打玩家资源点"),
	FragmentSweep(27,false,false,false,"碎片扫荡"),
	ARENA(28,false,false,false,"竞技场"),
	BATTLE(29,true,true,true,"首都争夺战"),
	
	BlackHawkRaid(30,true,true,false,"黑鹰军，战斗"),
	MonsterBoss(31,true,true,false,"叛军boss"),
	;
	
	private WarType(int type, boolean atkArmyLoss, boolean defArmyLoss, boolean haveWornCon, String desc) {
		this.type = type;
		this.atkArmyLoss = atkArmyLoss;
		this.defArmyLoss = defArmyLoss;
		this.haveWornCon = haveWornCon;
		this.desc = desc;
	}


	private int type;
	private boolean atkArmyLoss;//攻击方是否计算战损
	private boolean defArmyLoss;//防守方是否计算战损
	private boolean haveWornCon;//是否有军工
	private String desc;
	
	public int getType() {
		return type;
	}
	public boolean isAtkArmyLoss() {
		return atkArmyLoss;
	}
	public boolean isDefArmyLoss() {
		return defArmyLoss;
	}
	public boolean isHaveWornCon() {
		return haveWornCon;
	}
	public String getDesc() {
		return desc;
	}
	
	public static WarType getWarType(int type) {
		for (WarType temp : WarType.values()) {
			if(type == temp.getType()) return temp; 
		}
		return null;
	}
}
