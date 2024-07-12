package com.hm.enums;


public enum OverallWarState {
	Running(1, "进行中"),
	Close(2, "关闭中"),
	;
	private OverallWarState(int type, String desc) {
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
	public static OverallWarState getHangUpSet(int id){
		for(OverallWarState set : OverallWarState.values()){
			if(set.getType() == id){
				return set;
			}
		}
		return null;
	}
	
}
