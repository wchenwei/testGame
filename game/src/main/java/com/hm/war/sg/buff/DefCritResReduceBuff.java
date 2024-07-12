package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

public class DefCritResReduceBuff extends BaseBuffer{
	public DefCritResReduceBuff(long endFrame, double val) {
		super(UnitBufferType.DefCritResReduce,endFrame,BuffKind.Buff);
		this.setValue(val);
	}
	
	@Override
	public boolean isCanReplace(Frame frame, BaseBuffer newBuff) {
		return newBuff.isForoverBuff() || newBuff.getEndFrame() > getEndFrame();
	}
}
