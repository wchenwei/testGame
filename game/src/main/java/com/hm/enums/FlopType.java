package com.hm.enums;

public enum FlopType {
	FlopEnd(1, "结束翻牌"),
	FlopAll(2, "全翻"),
	;

	private FlopType(int type, String desc) {
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
