package com.hm.enums;

public enum AtkDefType {
	ATK(0,"进攻方"),
	DEF(1,"防御方"),
	;
	
	private AtkDefType(int type, String desc) {
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
}