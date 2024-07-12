package com.hm.enums;

public enum ResultType {
	NONE(0,"NONE"),
	WIN(1,"胜利"),
	FAIL(2,"失败"),
	;
	
	private ResultType(int type, String desc) {
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