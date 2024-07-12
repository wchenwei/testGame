package com.hm.enums;

public enum TroopPosition {
	None(0,"默认位置，处于玩家掌控"),
	GuildBarrack(1,"部落大营，处于部落官员掌控"),
	CampBarrack(2,"阵营大营"),
	;
	
	private TroopPosition(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
