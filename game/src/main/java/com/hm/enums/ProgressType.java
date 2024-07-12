package com.hm.enums;

/**
 * 
 * @Description: 进度状态
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum ProgressType {
	None(0,"未解锁"),
	WaitOpen(1,"等待开启"),
	Running(2,"进行中"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private ProgressType(int type, String desc) {
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
