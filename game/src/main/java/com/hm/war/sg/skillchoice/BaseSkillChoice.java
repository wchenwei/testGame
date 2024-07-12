package com.hm.war.sg.skillchoice;

import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public abstract class BaseSkillChoice {
	public abstract List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup);
	
	public void loadParm(String parm) {
		
	}
}
