package com.hm.enums;




/**
 * 邮件类型
 * @author yl
 * @version 2013-3-2
 *
 */
public enum DriverSkillType {
	One(1,"自己"),
	All(2,"全体"),
	War(3,"场内"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private DriverSkillType(int type, String desc) {
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
	
	public static DriverSkillType getType(int type){
		for (DriverSkillType kind : DriverSkillType.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}
	
	
}
