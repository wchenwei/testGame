package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class OfferHpEffect extends BaseSkillEffect{
	
	public OfferHpEffect() {
		super(SkillEffectType.OfferHpEffect);
	}
	
	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		unitList.forEach(myUnit -> {
			HurtBear bear = new HurtBear(myUnit.getId(), frame.getId()+skillFunction.getEffectFrame(), 
					getValue(myUnit,skill,skillFunction),skill.getId(),skillFunction.getId());
			myUnit.addBear(frame,bear);
		});
	}
	
	
	public long getValue(Unit atk,Skill skill,SkillFunction skillFunction) {
		//牺牲百分比
		double offer = skillFunction.getFunctionValue(atk,atk,skill);
		return (long)(atk.getMaxHp()*offer);
	}
}