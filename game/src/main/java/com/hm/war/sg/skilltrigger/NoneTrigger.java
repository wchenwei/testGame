package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

public class NoneTrigger extends BaseTriggerSkill{
	public NoneTrigger() {
		super(SkillTriggerType.None);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		return true;
	}
}
