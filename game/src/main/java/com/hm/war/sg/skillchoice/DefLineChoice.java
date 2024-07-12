package com.hm.war.sg.skillchoice;

import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class DefLineChoice extends BaseSkillChoice{
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		return ChoiceUtils.listUnitsByIndexs(ChoiceUtils.getLinesByIndex(atk.getIndex()), atkGroup.getLifeUnit());
	}
	
}
