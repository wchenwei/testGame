package com.hm.enums;


public enum LevelEventRewardType {
	TargetReward(1, "目标奖励"),
	ProgressReward(2, "进度奖励"),
	StarFullReward(3, "满星奖励"),
	;

	private LevelEventRewardType(int type, String desc) {
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
	
	public static LevelEventRewardType getType(int type){
		for(LevelEventRewardType levelEventRewardType:LevelEventRewardType.values()){
			if(levelEventRewardType.getType() == type){
				return levelEventRewardType;
			}
		}
		return null;
	}
	
}
