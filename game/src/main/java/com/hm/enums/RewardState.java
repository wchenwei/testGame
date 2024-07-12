package com.hm.enums;

public enum RewardState {
	NoReward(0,"不能领取"),
	CanReward(1,"可以领取"),
	AlreadyReward(2,"已经领取"),
	;
	
	private RewardState(int state, String desc) {
		this.state = state;
		this.desc = desc;
	}
	private int state;
	private String desc;

	public int getState() {
		return state;
	}

	public String getDesc() {
		return desc;
	}
}
