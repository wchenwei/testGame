package com.hm.enums;

/**
 * 
 * @Description: 城池状态
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum CastleStatus {
	Campaign(0,"竞选阶段"),
	ReadyRebuild(1,"准备重建阶段"),
	ASCRIPTION(2,"已经有归属状态"),
	
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private CastleStatus(int type, String desc) {
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
