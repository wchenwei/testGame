package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula30 extends BaseCalFormula{
	
	//（（释放者攻击力-目标防御力）*系数A+系数B）*施放者暴击伤害
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		long atkValue = atk.getUnitAttr().getLongValue(TankAttrType.ATK);
		long defValue = def.getUnitAttr().getLongValue(TankAttrType.DEF);
		double critValue = atk.getUnitAttr().getDoubleValue(TankAttrType.CritDamPer);
		
		double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
		double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());
		
		double skillHurt = (Math.max(atkValue-defValue, 0)*p1+p2)*critValue;
		return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
	}
}
