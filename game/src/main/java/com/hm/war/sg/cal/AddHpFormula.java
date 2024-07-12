package com.hm.war.sg.cal;

import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class AddHpFormula extends BaseCalFormula{
	
	//释放者的攻击 x系数A
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double skillHurt = p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
