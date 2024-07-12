package com.hm.enums;

public enum RaidBossState {
	Normal(0,"正常"),
	Active(1,"已激活"),
	Death(2,"已死亡"),
	;
	
	private RaidBossState(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	public static boolean isAlive(int bossState) {
		return Normal.getType()==bossState||Active.getType()==bossState;
	}
}