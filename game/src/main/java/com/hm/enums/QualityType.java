package com.hm.enums;

public enum QualityType {
	NONE(0, "未知","000000"),
	WHITE(1, "白色","d6d6d6"),
	GREEN(2, "绿色","92de30"),
	BLUE(3, "蓝色","64b3f7"),
	PURPLE(4, "紫色","cd97ff"),
	ORANGE(5, "金色","fff82c"),
	RED(6, "红色","ff6666"),
	;
	
	private QualityType(int type,  String desc,String color) {
		this.type = type;
		this.desc = desc;
		this.color = color;
	}
	
	private final int type;
	
	private final String desc;
	private final String color;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	public String getColor(){
		return color;
	}
	public static QualityType getType(int type) {
		for (QualityType buildType : QualityType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return NONE;
	}
}
