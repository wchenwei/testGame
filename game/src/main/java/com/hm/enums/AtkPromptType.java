package com.hm.enums;

/**
 * 
 * @Description: 攻击提示类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum AtkPromptType {
	AtkBuild(1,"攻击自己基地提示"),
	AtkResource(2,"攻击自己的矿产提示"),
	SpyonBuild(3,"侦查自己基地提示"),
	SpyonResource(4,"侦查自己的矿产提示"),
	AtkGarrisonBuild(5,"攻击驻防基地提示"),
	SpyonGarrisonBuild(6,"侦查驻防基地提示"),
	;
	
	private AtkPromptType(int type, String desc) {
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
