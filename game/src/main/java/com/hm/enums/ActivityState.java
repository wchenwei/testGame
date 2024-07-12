package com.hm.enums;

/**
 * 
 * @Description: 活动状态
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum ActivityState {
	Normal(0,"正常"),
	CalOver(1,"结算完成"),
	End(2,"已经结束"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private ActivityState(int type, String desc) {
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
