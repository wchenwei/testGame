package com.hm.enums;

import com.google.common.collect.Lists;

/**
 * 
 * @Description: 等级事件支援类型
 * @author xjt  
 * @date 2021年2月3日17:24:01
 * @version V1.0
 */
public enum EventSupportType {
	AirSupport(1,"空军支援"),
	ArtillerySupport(2,"炮兵支援"),
	EngineerSupport(3,"工兵支援"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private EventSupportType(int type, String desc) {
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

	public static EventSupportType getType(int type) {
		return Lists.newArrayList(EventSupportType.values()).stream().filter(t -> t.getType()==type).findFirst().orElse(null);
	}
}
