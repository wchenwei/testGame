package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.event.ReviveEvent;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class ReviveEffect extends BaseSkillEffect{
	
	public ReviveEffect() {
		super(SkillEffectType.ReviveEffect);
	}
	
	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		unitList.forEach(def -> {
			long addHp = (long)getValue(unit, def, skill, skillFunction);
			def.getHpEngine().setInitHp(addHp);
			//复活
			frame.addEvent(new ReviveEvent(def,skillFunction.getId()));
		}
		);
	}
	
	public double getValue(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return skillFunction.getFunctionValue(atk,def,skill);
	}
}
