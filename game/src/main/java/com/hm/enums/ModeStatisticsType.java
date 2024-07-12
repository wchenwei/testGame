package com.hm.enums;


/**
 * 
 * @Description: 模块统计
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum ModeStatisticsType {
	DropMode(1,"掉落统计模块"),
	
	;
	
	private ModeStatisticsType(int type, String desc) {
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
