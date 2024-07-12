package com.hm.enums;

public enum ValentineEventType {
	Welfare(1,"福利"),
	Fight(2,"战斗"),
	Shop(3,"商店"),
	Cost(4,"消耗"),
	Lucy(5,"幸运"),
	Gift(6,"计费礼包"),
	;
	private ValentineEventType(int type,String desc){
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
