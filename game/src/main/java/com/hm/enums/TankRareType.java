package com.hm.enums;


public enum TankRareType {
	N(1,"N"),
	R(2,"R"),
	SR(3,"SR"),
	SSR(4,"SSR"),
	SSSR(5,"SSSR"),
	;
	
	private TankRareType(int type, String desc) {
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
	
	public static TankRareType getType(int type) {
		for (TankRareType buildType : TankRareType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return N;
	}
}
