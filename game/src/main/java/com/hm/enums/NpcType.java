package com.hm.enums;

/**
 * 
 * @Description: npc类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum NpcType {
	CityDef(0,"城池防守"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private NpcType(int type, String desc) {
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
