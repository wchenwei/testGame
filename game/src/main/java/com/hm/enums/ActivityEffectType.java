package com.hm.enums;

public enum ActivityEffectType {
	Exp(1,"经验加成"),
	TankExp(2,"坦克经验加成"),
	Reward(3,"奖励加成"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private ActivityEffectType(int type, String desc) {
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
