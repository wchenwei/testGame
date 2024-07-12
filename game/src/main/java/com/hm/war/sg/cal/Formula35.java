package com.hm.war.sg.cal;

import com.hm.util.MathUtils;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula35 extends BaseCalFormula{
	
	//【58.128*(攻速降低百分比)^2 + 8.9686*(攻速降低百分比) + 0.6896 】 最小为1
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		double baseCD = MathUtils.div(1, atk.getSetting().getBaseAtkCd());
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		return -(p1*baseCD);
	}
}
