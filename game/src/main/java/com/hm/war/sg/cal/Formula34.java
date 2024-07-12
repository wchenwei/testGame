package com.hm.war.sg.cal;

import com.hm.util.MathUtils;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula34 extends BaseCalFormula{
	
	//向下求整【3.6471*ln(攻速提升百分比) + 10.365）】    
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		double baseCD = MathUtils.div(1, atk.getSetting().getBaseAtkCd());
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		return p1*baseCD;
	}
}
