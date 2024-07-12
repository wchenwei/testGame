package com.hm.war.sg.cal;

import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula24 extends BaseCalFormula{
	// 目标能量上限*系数a
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		double value = def.getMpEngine().getMaxMp();
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double skillHurt = value*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
