package com.hm.enums;

public enum PushType {
//	HourReward(1, "领取石油推送"), 
	MineRob(6, "跨服资源战被击败"),
	TroopHangUpEnd(9, "挂机结束推送"), 
	;
	
	private PushType(int type, String desc) {
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
