package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

public abstract class BaseTriggerSkill {
	private SkillTriggerType type;
	
	public BaseTriggerSkill(SkillTriggerType type) {
		this.type = type;
	}

	public boolean isCanTrigger(Unit atk, Unit def, Object...args) {
		return isCanTriggerFrame(null, atk, def, args);
	}
	
	public abstract boolean isCanTriggerFrame(Frame frame,Unit atk,Unit def,Object...args);
	
	public void init(String parms,int lv) {
		
	}

	public SkillTriggerType getType() {
		return type;
	}
	
}
