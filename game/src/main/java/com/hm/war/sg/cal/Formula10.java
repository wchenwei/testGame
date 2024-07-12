package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula10 extends BaseCalFormula{
	
	//受到伤害*系数a
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long atkValue = atk.getUnitAttr().getLongValue(TankAttrType.ATK);
		long defValue = def.getUnitAttr().getLongValue(TankAttrType.DEF);
		long defHpMax = def.getMaxHp();
		
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		
		double skillHurt = (atkValue-defValue)*p1+defHpMax*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
