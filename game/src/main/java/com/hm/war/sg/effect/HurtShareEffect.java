package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.BuffBear;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class HurtShareEffect extends BaseSkillEffect{

	public HurtShareEffect() {
		super(SkillEffectType.HurtShareEffect);
	}
	
	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList,Skill skill,SkillFunction skillFunction) {
		if(unitList.size() < 2) {
			return;
		}
		Unit def1 = unitList.get(0);
		Unit def2 = unitList.get(1);
		if(def1.getId() == def2.getId()) {
			return;
		}
		double value = getValue(unit, def1, skill, skillFunction);

		addBuff(frame,def1,def2,skillFunction,value);
		addBuff(frame,def2,def1,skillFunction,value);
	}

	public double getValue(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return skillFunction.getFunctionValue(atk,def,skill);
	}

	//转移伤害buff
	public void addBuff(Frame frame,Unit unit,Unit def,SkillFunction skillFunction,double value) {
		long continueFrame = skillFunction.getContinuedFrame();
		long effectFrame = frame.getId()+skillFunction.getEffectFrame() + skillFunction.getDelayFrame();

		BuffBear bear = new BuffBear(unit.getId(), UnitBufferType.HurtSelfLink, continueFrame,effectFrame,skillFunction);
		bear.setValue(value);
		def.addBear(frame,bear);
	}
}
