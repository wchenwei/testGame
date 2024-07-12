package com.hm.war.sg.unit;

/**
 * 
 * @Description: 战斗单元额外类型
 * @author siyunlong  
 * @date 2020年7月26日 上午1:33:29 
 * @version V1.0
 */
public enum UnitExtraType {
	StrangeTank(1,"奇兵坦克"),
	CallSSSStrangeTank(2,"召唤了奇兵的坦克"),
	;
	
	private UnitExtraType(int type, String desc) {
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
