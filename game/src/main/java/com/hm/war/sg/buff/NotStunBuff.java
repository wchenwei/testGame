package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

public class NotStunBuff extends BaseBuffer{
	public NotStunBuff(long endFrame) {
		super(UnitBufferType.NotStunBuff,endFrame,BuffKind.Buff);
	}
	
	@Override
	public boolean isCanReplace(Frame frame, BaseBuffer newBuff) {
		return newBuff.isForoverBuff() || newBuff.getEndFrame() > getEndFrame();
	}
}
