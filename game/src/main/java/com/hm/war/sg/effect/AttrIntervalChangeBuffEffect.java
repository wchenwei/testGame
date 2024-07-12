package com.hm.war.sg.effect;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.AttrIntervalChangeBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class AttrIntervalChangeBuffEffect extends BaseSkillEffect{
	private TankAttrType attrType; 
	
	public AttrIntervalChangeBuffEffect(TankAttrType attrType) {
		super(SkillEffectType.AttrBuff);
		this.attrType = attrType;
	}
	
	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		unitList.forEach(def -> 
			{
				long effectFrame = frame.getId();
				double addAttr = getAttrValue(unit,def,skill, skillFunction);
				def.addBear(frame,new AttrIntervalChangeBear(unit.getId(), effectFrame, attrType, addAttr,skillFunction));
			}
		);
	}
	
	public double getAttrValue(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return skillFunction.getFunctionValue(atk,def,skill);
	}
}
