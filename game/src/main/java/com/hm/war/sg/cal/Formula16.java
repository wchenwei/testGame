package com.hm.war.sg.cal;

import com.hm.util.MathUtils;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula16 extends BaseCalFormula{
	
	//（释放者血量上限-剩余血量）/（施放者血量上限*0.1））】*系数A
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long maxHp = atk.getMaxHp();
		long hp = atk.getHp();
		
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		
		double skillHurt = MathUtils.div(maxHp-hp, maxHp*0.1)*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
