package com.hm.war.sg.setting;

import cn.hutool.core.util.RandomUtil;

/**
 * @Description: 技能触发
 * @author siyunlong  
 * @date 2018年9月27日 下午4:43:06 
 * @version V1.0
 */
public enum SkillTouchType {
	None(0,false,"none"),
	HandTouch(1,false,"大技能，能量满手动或自动触发"),
	NormalAtkTouch(2,true,"普攻时概率触发，不会替代普通，普攻生效时间等于技能生效时间，普通伤害仍然生效"),
	DeathTouch(3,false,"死亡时触发"),
	Commander(100,false,"指挥官技能，倒计时到了即可点击触发，也可自动触发,算伤害时，攻击力=所有己方战车攻击力之和"),
	;
	
	private SkillTouchType(int type, boolean isRate, String desc) {
		this.type = type;
		this.desc = desc;
		this.isRate = isRate;
	}
	private int type;
	private String desc;
	private boolean isRate; 
	
	public static SkillTouchType getType(int type) {
		for (SkillTouchType buildType : SkillTouchType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	
	public boolean isCanTrigger(double rate) {
		return !isRate || RandomUtil.randomDouble() < rate;
	}
}
