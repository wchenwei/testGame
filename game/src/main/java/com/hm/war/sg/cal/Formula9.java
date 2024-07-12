package com.hm.war.sg.cal;

import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula9 extends BaseCalFormula{
	
	//目标当前能量*系数a
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		double mp = def.getMpEngine().getMp();
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		
		double skillHurt = mp*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
