package com.hm.enums;

public enum ServerType {
	Game(0,"游戏服务器"),
	KuaFu(1,"跨服服务器"),
	;
	
	private ServerType(int type, String desc) {
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