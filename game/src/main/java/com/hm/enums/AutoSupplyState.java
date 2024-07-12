package com.hm.enums;

/**
 * 
 * @Description: 回复状态
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum AutoSupplyState {
	LOCK(-1,"没开放"),
	CLOSE(0,"关闭"),
	OPEN(1,"开放"), 
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private AutoSupplyState(int type, String desc) {
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
	
	public static AutoSupplyState getType(int type){
		if(type == LOCK.getType()) return LOCK; 
		if(type == CLOSE.getType()) return CLOSE; 
		if(type == OPEN.getType()) return OPEN; 
		return null; 
	}
}
