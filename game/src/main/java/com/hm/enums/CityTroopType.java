package com.hm.enums;

/**
 * 
 * @Description: 城镇部队类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum CityTroopType {
	PlayerTroop(0,"玩家部队"),
	NpcTroop(1,"npc部队"),
	ClonePlayer(2,"玩家镜像"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private CityTroopType(int type, String desc) {
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
