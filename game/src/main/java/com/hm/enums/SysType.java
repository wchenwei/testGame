package com.hm.enums;

/**
 * 
 * @Description: 操作系统类型
 * @author zxj  
 * @version V1.0
 */
public enum SysType {
	ANDROID("android","安卓"),
	IOS("ios","苹果"),
	
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private SysType(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private String type;
	
	private String desc;

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	public static SysType getSysType(String type){
		for (SysType kind : SysType.values()) {
			if(kind.getType().equals(type)) return kind; 
		}
		return null;
	}
}
