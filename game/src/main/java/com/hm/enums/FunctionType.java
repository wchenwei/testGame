package com.hm.enums;

/**
 * @Description: 功能性道具
 * @author siyunlong  
 * @date 2018年4月11日 下午5:40:46 
 * @version V1.0
 */
public enum FunctionType {
    RedPacket(1,"红包"),
	SeasonVipCard(2,"至尊季卡-体验"),
	QywxMark(3,"企业微信标识"),

	;
	
	private FunctionType(int type,  String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	private final int type;
	
	private final String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	public static FunctionType getType(int type) {
		for (FunctionType functionType : FunctionType.values()) {
			if(functionType.getType() == type) {
				return functionType;
			}
		}
		return null;
	}
	
}
