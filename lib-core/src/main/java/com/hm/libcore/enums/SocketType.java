package com.hm.libcore.enums;


public enum SocketType {
	Normal(0,"正常客户端socket"),
	H5(1,"H5客户端socket"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private SocketType(int type, String desc) {
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
