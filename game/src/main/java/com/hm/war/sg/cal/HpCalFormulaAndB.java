package com.hm.war.sg.cal;

import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class HpCalFormulaAndB extends BaseCalFormula{
	private boolean isAtk;
	
	public HpCalFormulaAndB(boolean isAtk) {
		this.isAtk = isAtk;
	}

	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		Unit temp = isAtk?atk:def;
		long value = temp.getHp();
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());
		double skillHurt = value*p1 + p2;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
