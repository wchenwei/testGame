/**
 * 
 */
package com.hm.enums;

/**
 * Title: WarUnitType.java
 * Description:战斗单位类型 用于标记附件属性
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2015年4月30日 上午10:36:17
 * @version 1.0
 */
public enum SysNoticeType {
	
	PER_LV_UP(1,"统率升级"),
	STR(2,"强化"),
	PVE_UNLOCK(3,"PVE关卡解锁"),
	LOTTY(4,"抽奖"),
	REF(5,"改造"),
	GUILD_LV_UP(6,"部落升级"),
	ARENA_WIN(7,"竞技场挑战连胜"),
	OVER_ARENA_WIN(8,"终结连胜"),
	ACTIVITY_OPEN(10,"活动开启"),
	TERR_OPEN(11,"领地战开启"),
	COMMANDER_GET(12,"将领抽取"),
	COMMANDER_QUE_UP(13,"将领突破"),
	VIP(14,"vip等级提升"),
	WORLDTASKOPEN(15,"世界任务开始"),
	WORLDTASKTOOVER(16,"世界任务即将完成"),
	WORLDTASKOVER(17,"世界任务完成"),
	WORLDTASKNOOVER(18,"世界任务未完成");
	
	
	private SysNoticeType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	
	private String desc;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
