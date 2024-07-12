package com.hm.war.sg.cal;

import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula28 extends BaseCalFormula{
	
	//（目标血量上限-目标剩余血量）*系数A
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long maxHp = def.getMaxHp();
		long hp = def.getHp();
		
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		
		double skillHurt = Math.max(maxHp-hp, 0)*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
