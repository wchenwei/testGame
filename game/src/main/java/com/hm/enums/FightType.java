package com.hm.enums;

/**
 * 
 * @Description: 战斗类型
 * @author siyunlong  
 * @date 2019年1月16日 下午6:48:16 
 * @version V1.0
 */
public enum FightType {
	All(-1,"永久生效"),
	AtkCity(1,"攻城中生效"),
	DefCity(2,"守城中生效"),
	Pvp(3,"偷袭中生效"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private FightType(int type, String desc) {
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
