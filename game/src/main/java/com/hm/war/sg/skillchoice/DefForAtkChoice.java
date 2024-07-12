package com.hm.war.sg.skillchoice;

import com.google.common.collect.Lists;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class DefForAtkChoice extends BaseSkillChoice{
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		Unit curAtkMe = atk.getCurAtkMe();
        if (curAtkMe != null && !curAtkMe.isDeath()) {
			return Lists.newArrayList(curAtkMe);
		}
		return Lists.newArrayList();
	}
	
}
