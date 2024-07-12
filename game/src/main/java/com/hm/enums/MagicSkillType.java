
package com.hm.enums;

public enum MagicSkillType {
	Normal(0,"魔改普通技能"),
	BigSkill(1,"一阶大招"),
    SuperBigSkill(2,"二阶大招"),

	;
	private MagicSkillType(int type, String desc){
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

	public static MagicSkillType getType(int type) {
		for (MagicSkillType buildType : MagicSkillType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	
}

