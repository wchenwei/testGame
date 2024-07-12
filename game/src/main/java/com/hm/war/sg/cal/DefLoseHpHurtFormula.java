package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class DefLoseHpHurtFormula extends BaseCalFormula{
	
	//（目标血量上限-目标剩余血量）*系数A+系数B
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long defHpMax = def.getUnitAttr().getLongValue(TankAttrType.HP);
		long hp = def.getHp();
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());
		
		double skillHurt = (defHpMax-hp)*p1+p2;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}