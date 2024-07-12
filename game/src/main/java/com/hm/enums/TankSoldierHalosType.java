package com.hm.enums;

public enum TankSoldierHalosType {
	EvolveCircle(1,"进化光环"),
	masteryCircle(2,"专精光环"),
	MagicCircle(3,"魔改光环"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private TankSoldierHalosType(int type, String desc) {
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
