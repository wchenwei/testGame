package com.hm.enums;

/**
 * @Description: 部落指挥类型
 * @author siyunlong  
 * @date 2018年2月24日 上午10:26:11 
 * @version V1.0
 */
public enum GuildCommandType {
	CommandAtk(1,"进攻"),
	CommandDef(2,"进攻"),
	CommandLine(3,"规划路线"),
	;
	
	private GuildCommandType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public int getType() {
		return type;
	}
	
	public static GuildCommandType getType(int type) {
		for (GuildCommandType buildType : GuildCommandType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	
}
