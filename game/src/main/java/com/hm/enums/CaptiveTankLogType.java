package com.hm.enums;

/**
 * 
 * @Description: 俘虏日志
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum CaptiveTankLogType {
	CaptiveOther(1,"我俘虏别人"),
	CaptiveMe(2,"别人俘虏我"),
	CaptiveTech(3,"战俘坦克研究"),
	;
	
	private CaptiveTankLogType(int type, String desc) {
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
