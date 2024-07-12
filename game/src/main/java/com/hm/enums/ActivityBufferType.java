package com.hm.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ActivityBufferType {
	MainCityLevel(1, "主城升级",1),
	MonsterSpeed(4, "叛军加速",0),
	;

	private ActivityBufferType(int type, String desc,int openType) {
		this.type = type;
		this.desc = desc;
		this.openType = openType;
	}
	
	private int type;
	private String desc;
	//开放类型, 0-后台开发  1-永久开发
	private int openType;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	public boolean isForeverType() {
		return openType == 1;
	}
	/**
	 * 获取永久开放的活动
	 * @return
	 */
	public static List<ActivityBufferType> getForeverActivity() {
		return Arrays.stream(ActivityBufferType.values()).filter(e -> e.isForeverType()).collect(Collectors.toList());
	}
}
