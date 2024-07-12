package com.hm.enums;

public enum BuildQueueType {
	None(0,"未知"),
	Normal(1,"正常建筑队列"),
	Queue2(2,"正常建筑队列"),
	Queue3(3,"正常建筑队列"),
	;
	
	private BuildQueueType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public int getType() {
		return type;
	}
	
}
