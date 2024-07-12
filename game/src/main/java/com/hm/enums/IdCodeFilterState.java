package com.hm.enums;

public enum IdCodeFilterState {
	/**
	 * type>=0代表该唯一码剩余可绑定的数量
	 */
	Normal(-1,"无理由放过(可能是充过值，或者不需要过滤)"),
	IpLimit(-2,"该ip下登录角色数已达上限"),
	Binded(-3,"玩家已经绑定过唯一码"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private IdCodeFilterState(int type, String desc) {
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
