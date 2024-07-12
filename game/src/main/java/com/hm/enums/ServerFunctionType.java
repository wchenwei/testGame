package com.hm.enums;

public enum ServerFunctionType {
	OverallWar(1,"全面战争"),
	WorldBoss(2,"世界boss"),
	RepairTrain(3,"维修训练"),
	EliminatePlay(4,"消除游戏"),
	KFServer(5,"开服开启"),

	;
	
	private ServerFunctionType(int type,String desc){
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
	
	public static ServerFunctionType getType(int type) {
		for (ServerFunctionType temp : ServerFunctionType.values()) {
			if(type == temp.getType()) return temp; 
		}
		return null;
	}
}
