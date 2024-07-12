package com.hm.war.sg.cal;

import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class NoneFormula extends BaseCalFormula{
	
	//释放者的攻击 x系数A + 系数B
	@Override
	public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
		return 0;
	}
}
