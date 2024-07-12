package com.hm.enums;

public enum ResetType {
	Normal(1, "自然重置"),
	Manual(2, "手动重置"),
	;
	
	private ResetType(int type,String desc) {
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
