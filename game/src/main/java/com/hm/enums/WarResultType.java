package com.hm.enums;

/**
 * 
 * @Description: 战斗报告类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum WarResultType {
	Normal(0,"正常城战"),
	Arena(1,"竞技场"),
	SupplyRob(2,"补给掠夺战"),
	OverallWar(3,"全面战争"),
	Stock(4,"大股东"),
	Boss(5,"世界boss"),

	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private WarResultType(int type, String desc) {
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
