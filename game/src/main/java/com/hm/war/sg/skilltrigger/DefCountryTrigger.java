package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

public class DefCountryTrigger extends BaseTriggerSkill{
	private int country;

	public DefCountryTrigger() {
		super(SkillTriggerType.Country2);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		return def.getSetting().getCountry() == country;
	}
	
	public void init(String parms,int lv) {
		this.country = Integer.parseInt(parms);
	}
}
