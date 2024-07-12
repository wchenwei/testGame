package com.hm.war.sg.bear;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.AttrBuff;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.AttrEvent;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.skilltrigger.BaseTriggerSkill;
import com.hm.war.sg.unit.Unit;

public class AttrBear extends Bear{
	protected TankAttrType attrType; 
	protected double value;
	protected SkillFunction skillFunction;
	
	public AttrBear(int atkId, long effectFrame, TankAttrType attrType,double value,SkillFunction skillFunction) {
		super(atkId, effectFrame);
		this.attrType = attrType;
		this.value = value;
		this.skillFunction = skillFunction;
	}

	@Override
	public Event createEvent(Unit unit) {
		return new AttrEvent(unit, attrType, skillFunction.getContinuedFrame(), value, skillFunction.getId());
	}

	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		BaseTriggerSkill buffTriggerCondition = skillFunction.getBuffTriggerCondition();
		long endFrame = skillFunction.getContinuedFrame() < 0 ?-1:frame.getId()+skillFunction.getContinuedFrame();
		if(buffTriggerCondition == null) {
			//如果没有触发条件，//计算最大叠加层数
			unit.getUnitBuffs().resetSkillBuffOverTimes(skillFunction.getId(), skillFunction.getMaxAttBuffOvers(), UnitBufferType.AttrBuff);
		}
		AttrBuff buff = new AttrBuff(endFrame, attrType, value,getAtkId(),unit.getId(),skillFunction.getId(),skillFunction.getBuffKind());
		buff.setBuffUnitId(getAtkId());
		buff.setBuffTriggerCondition(buffTriggerCondition);
		buff.setDelBuffType(skillFunction.getDelBuffType());
		unit.getUnitBuffs().addBuff(buff);
		if(attrType == TankAttrType.HP) {
			unit.getHpEngine().addHp((long)value);
		}
		return true;
	}
	
}