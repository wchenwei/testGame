package com.hm.war.sg.effect;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class ReduceAttrBufferEffect extends BaseAttrBufferEffect{

	public ReduceAttrBufferEffect(TankAttrType attrType) {
		super(attrType);
	}
	
	
	public double getAttrValue(Unit atk, Unit def, Skill skill, SkillFunction skillFunction) {
		return -skillFunction.getFunctionValue(atk,def,skill);
	}
}
