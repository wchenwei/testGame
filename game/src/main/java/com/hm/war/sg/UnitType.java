package com.hm.war.sg;



public enum UnitType {
	PlayerTank(0,"玩家坦克"),
	PvpNpc(1,"PVP_NPC"),
	PveNpc(2,"PVE_NPC"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private UnitType(int type, String desc) {
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
	
	public static UnitType getType(int type){
		for (UnitType kind : UnitType.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}
	
	
	
}
