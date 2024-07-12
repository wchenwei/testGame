package com.hm.war.sg.setting;

public class TouchSetting {
	private SkillTouchType skillTouchType;//触发类型
	private double baseRate;
	private double lvUp;//等级成长
	
	
	public TouchSetting(SkillTouchType skillTouchType) {
		super();
		this.skillTouchType = skillTouchType;
	}
	
	
	public boolean isCanTrigger() {
		return false;
	}
}
