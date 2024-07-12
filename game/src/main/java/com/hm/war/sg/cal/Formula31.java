package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula31 extends BaseCalFormula{
	
	//（释放者攻击力*系数A+系数B）*敌方当前数量
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long atkValue = atk.getUnitAttr().getLongValue(TankAttrType.ATK);
		long defNum = atk.getDefGroup().getLifeUnit().size();
		
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());
		
		double skillHurt = (atkValue*p1 + p2)*defNum;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
