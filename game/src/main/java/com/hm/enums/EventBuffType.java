package com.hm.enums;

import com.google.common.collect.Lists;

/**
 * 
 * @Description: 事件点buff类型
 * @author xjt  
 * @date 2021年2月3日17:24:01
 * @version V1.0
 */
public enum EventBuffType {
    Skill(1, "获得一个技能"),
    Recovery(2, "紧急维修，所有血量最少的N辆未损毁战车恢复20%耐久"),
    Revive(3, "战车工厂复活损毁的战力最高的N辆战车并恢复30%耐久"),
    Airdrop(4, "空投"),
    Reward(5, "悬赏"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private EventBuffType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	
	private int effectType;//0-获得生效一次 1-永久生效
	private int buffType;//0-增益 1-减益
	
	private String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public static EventBuffType getType(int type) {
		return Lists.newArrayList(EventBuffType.values()).stream().filter(t -> t.getType()==type).findFirst().orElse(null);
	}
}
