package com.hm.enums;

/**
 * @Description: 全服广播类型
 * @author siyunlong  
 * @date 2020年3月16日 下午6:07:10 
 * @version V1.0
 */
public enum AllServerBroadType {
	None(0,"测试"),
	OneDollarRoundOver(1,"1元夺宝结束一轮"),
	OneDollarAllIn(2,"1元夺宝买断"),

	TradeStockOwnerChange(3,"大股东变更"),
	;

	private AllServerBroadType(int type, String desc) {
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
