package com.hm.war.sg.cal;

import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula12 extends BaseCalFormula{
	
	//（释放者攻击力-目标防御力）*系数A+目标血量上限*系数A
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long value = atk.getHp();
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		
		double skillHurt = value*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
