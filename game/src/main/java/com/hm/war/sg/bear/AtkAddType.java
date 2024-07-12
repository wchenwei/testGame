package com.hm.war.sg.bear;

public enum AtkAddType {
	None(0,"正常"),
	Crit(1,"暴击"),
	Dodge(2,"闪避"),
	WardOff(3,"抵挡"),
	BackHurt(4,"反伤"),
	HolyHurt(5,"神圣伤害"),
	;
	
	private AtkAddType(int type, String desc) {
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
