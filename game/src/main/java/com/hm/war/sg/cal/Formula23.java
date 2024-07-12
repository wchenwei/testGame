package com.hm.war.sg.cal;

import com.hm.util.MathUtils;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula23 extends BaseCalFormula{
	
	// 【int（当前血量/（自己能量上限*0.1））】*系数A
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		int value = (int)MathUtils.div(atk.getMpEngine().getMp(), MathUtils.mul(atk.getMpEngine().getMaxMp(), 0.1));
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double skillHurt = value*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
