package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

public class CureEffectBuff extends BaseBuffer{
	
	public CureEffectBuff(long endFrame,double value) {
		super(UnitBufferType.CureEffectBuff,endFrame,BuffKind.None);
		setValue(value);
	}

	
	@Override
	public boolean isCanReplace(Frame frame,BaseBuffer buff) {
		return this.getFuncId() == buff.getFuncId();
	}
}