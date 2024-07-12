
package com.hm.enums;

public enum BuffType {
	ExperimentExpBuff(1,"闪电战经验加成"),
	ClassicExpBuff(2,"经典战役经验加成"),
	HonorExpBuff(3,"闪电战经验加成"),
	DailyTaskExpBuff(4,"日常任务经验加成"),
	ExpeditionBuff(5,"远征获得远征币加成"),
	ArenaExpBuff(6,"竞技场获得竞技币加成"),
	
	;
	private BuffType(int type,String desc){
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

	public static BuffType getType(int type) {
		for (BuffType buildType : BuffType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	
}

