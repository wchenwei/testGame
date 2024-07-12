package com.hm.enums;


public enum WarActionType {
	ACTIVE(1,"主动"),
	PASSIVE(2,"被动"),
	GARRISON(3,"协防"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private WarActionType(int type, String desc) {
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
