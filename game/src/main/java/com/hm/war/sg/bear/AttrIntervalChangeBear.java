package com.hm.war.sg.bear;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.AttrIntervalChangeBuff;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class AttrIntervalChangeBear extends AttrBear{
	public AttrIntervalChangeBear(int atkId, long effectFrame, TankAttrType attrType, double value, SkillFunction skillFunction) {
		super(atkId, effectFrame, attrType, value, skillFunction);
	}


	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		long endFrame = skillFunction.getContinuedFrame() < 0 ?-1:frame.getId()+skillFunction.getContinuedFrame();
		AttrIntervalChangeBuff buff = new AttrIntervalChangeBuff(endFrame, attrType, value,getAtkId(),unit.getId(),skillFunction.getId(),skillFunction.getBuffKind());
		//设置间隔帧
		buff.setNextAddValueInterval(frame,skillFunction.getIntervalFrame());
		buff.setDelBuffType(buff.getDelBuffType());
		buff.setBuffUnitId(getAtkId());
		unit.getUnitBuffs().addBuff(buff);
		return true;
	}
}