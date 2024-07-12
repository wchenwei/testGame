package com.hm.war.sg.skillchoice;

import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class AllSelfChoice extends BaseSkillChoice{
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		return atkGroup.getLifeUnit();
	}
	
}
