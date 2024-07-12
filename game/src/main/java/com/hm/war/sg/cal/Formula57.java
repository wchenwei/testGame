package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula57 extends BaseCalFormula{
	
	//释放者当前攻击力-目标当前防御力*系数A）*系数B+系数C
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long atkValue = atk.getUnitAttr().getLongValue(TankAttrType.ATK);
		long defValue = def.getUnitAttr().getLongValue(TankAttrType.DEF);
		
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());
		double p3 = skillFunction.getParmList().get(2).getLvValue(skillFunction.getLv());

		double skillHurt = (atkValue-defValue*p1)*p2+p3;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}