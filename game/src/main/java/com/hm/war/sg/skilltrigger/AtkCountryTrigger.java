package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

public class AtkCountryTrigger extends BaseTriggerSkill{
	private int country;

	public AtkCountryTrigger() {
		super(SkillTriggerType.Country);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		return atk.getSetting().getCountry() == country;
	}
	
	public void init(String parms,int lv) {
		this.country = Integer.parseInt(parms);
	}
}
