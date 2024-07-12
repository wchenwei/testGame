package com.hm.war.sg.skilltrigger;

import com.hm.util.RandomUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

public class RateTrigger extends BaseTriggerSkill{
	private double rate;

	public RateTrigger() {
		super(SkillTriggerType.Rate);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		return RandomUtils.randomIsRate(rate);
	}
	
	@Override
	public void init(String parms,int lv) {
		double base = Double.parseDouble(parms.split(":")[0]);
		double lvup = Double.parseDouble(parms.split(":")[1]);
		this.rate = base + (lv-1)*lvup;
	}
}
