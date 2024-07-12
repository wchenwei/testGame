package com.hm.enums;


public enum LevelEventTargetType {
	TargetReward(1, "目标奖励"),
	ProgressReward(2, "进度奖励"),
	LimitStep(3, "X步内占领全部路线"),
	;

	private LevelEventTargetType(int type, String desc) {
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
	
	public static LevelEventTargetType getType(int type){
		for(LevelEventTargetType levelEventRewardType:LevelEventTargetType.values()){
			if(levelEventRewardType.getType() == type){
				return levelEventRewardType;
			}
		}
		return null;
	}
	
}
