package com.hm.enums;


/**
 * 
 * @Description: 掉落模块
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum DropModeType {
	WarMode(1,"战斗掉落模块"),
	ActivityMode(2,"活动掉落模块"),
	ActivityModeRebelArmy(3,"活动叛军掉落模块"),
	GuildFactoryDropMode(4,"军工厂掉落模块"),
	;
	
	private DropModeType(int type, String desc) {
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
