package com.hm.war.sg.skillchoice;

import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class CountryDefChoice extends BaseSkillChoice{
	private int country;
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		return ChoiceUtils.getUnitByCountry(defGroup.getLifeUnit(), country);
	}
	
	@Override
	public void loadParm(String parm) {
		this.country = Integer.parseInt(parm);
	}
}
