package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class HpTopHurtFormula extends BaseCalFormula{
	
	//目标血量上限*系数A
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long defHpMax = atk.getUnitAttr().getLongValue(TankAttrType.HP);
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		
		double skillHurt = defHpMax*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}