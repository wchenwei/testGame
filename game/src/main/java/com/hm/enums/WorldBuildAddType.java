package com.hm.enums;

import java.util.Arrays;

/**
 * 
 * @Description: 世界建筑加成
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum WorldBuildAddType {
	TroopRecover(1,"本服部队回复速度加成"),
	WorldBuildDayReward(2,"世界建筑每日奖励"),
	ArenaAdd(3,"军演收益加成"),
	ExperimentBattleExpAdd(4,"闪击战经验加成"),
	ClassicBattlesItemAdd(5,"经典战道具加成"),
	ExpeditionMoneyAdd(6,"远征币加成"),
	TowerMoneyAdd(7,"使命之路币加成"),
	KFAttrAdd(8,"跨服属性加成"),
	KFUSAItemAdd(9,"跨服远征道具加成"),
	KFAiportAddNum(10,"跨服空投數量"),
	KFTroopRecover(11,"跨服部队回复速度加成"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private WorldBuildAddType(int type, String desc) {
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
	
	public static WorldBuildAddType getWorldBuildAddType(int type) {
		return Arrays.stream(WorldBuildAddType.values()).filter(t -> t.getType()==type).findFirst().orElse(null);
	}
}
