package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class AttrCalFormula extends BaseCalFormula{
	private TankAttrType attrType;
	private boolean isAtk;
	
	public AttrCalFormula(TankAttrType attrType,boolean isAtk) {
		this.attrType = attrType;
		this.isAtk = isAtk;
	}


	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		Unit temp = isAtk?atk:def;
		long value = temp.getUnitAttr().getLongValue(attrType);
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double skillHurt = value*p1;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
