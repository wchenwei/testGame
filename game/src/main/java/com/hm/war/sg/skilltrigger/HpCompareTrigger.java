package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

public class HpCompareTrigger extends BaseTriggerSkill{
	private boolean isLow;//
	
	public HpCompareTrigger(boolean isLow) {
		super(SkillTriggerType.HpCompare);
		this.isLow = isLow;
	}

	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		if(isLow) {
			return def.getHp() < atk.getHp();
		}else{
			return def.getHp() >= atk.getHp();
		}
	}
		
}
