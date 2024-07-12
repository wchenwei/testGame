package com.hm.war.sg;

/**
 * @Description: 战斗后的保留状态
 * @author siyunlong  
 * @date 2018年10月23日 下午4:16:33 
 * @version V1.0
 */
public enum UnitRetainType {
	HP(1,"血量"),
	MP(2,"蓝量"),
	;
	
	private UnitRetainType(int type, String desc) {
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
