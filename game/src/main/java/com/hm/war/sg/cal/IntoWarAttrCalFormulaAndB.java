package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: 进场属性
 * @author siyunlong  
 * @date 2020年11月15日 上午1:51:49 
 * @version V1.0
 */
public class IntoWarAttrCalFormulaAndB extends BaseCalFormula{
	private TankAttrType attrType;
	private boolean isAtk;
	
	public IntoWarAttrCalFormulaAndB(TankAttrType attrType,boolean isAtk) {
		this.attrType = attrType;
		this.isAtk = isAtk;
	}


	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		Unit temp = isAtk?atk:def;
		long value = temp.getIntoWarUnitAttr().getLongValue(attrType);
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());
		double skillHurt = value*p1 + p2;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
