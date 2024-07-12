package com.hm.enums;

/**
 * 
 * @Description: 回复状态
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum ApplyType {
	NONE(0,"未回复"),
	AGREE(1,"同意"),
	REFUSE(2,"拒绝"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private ApplyType(int type, String desc) {
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
