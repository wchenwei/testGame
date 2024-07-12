package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula25 extends BaseCalFormula{
	
	// 己方存活战车攻击总和*系数A+系数B加两个计算类型
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long atkSum = atk.getMyGroup().getLifeUnit().stream()
				.mapToLong(e -> e.getUnitAttrNoSkill().getLongValue(TankAttrType.ATK)).sum();
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());
		double skillHurt = atkSum*p1+p2;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
