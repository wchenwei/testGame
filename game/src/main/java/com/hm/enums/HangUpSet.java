package com.hm.enums;


public enum HangUpSet {
	Repair(1, "自动使用修理锤修理",0),
	Gold(2, "自动使用金砖修理",0),
	Advance(3, "自动突进",3),
	SneakAttack(4, "自动偷袭",3),
	;
	private HangUpSet(int type, String desc,int vipLimit) {
		this.type = type;
		this.desc = desc;
		this.vipLimit = vipLimit;
	}
	private int type;
	private String desc;
	private int vipLimit;
	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	public int getVipLimit() {
		return vipLimit;
	}
	public static HangUpSet getHangUpSet(int id){
		for(HangUpSet set : HangUpSet.values()){
			if(set.getType() == id){
				return set;
			}
		}
		return null;
	}
	
}
