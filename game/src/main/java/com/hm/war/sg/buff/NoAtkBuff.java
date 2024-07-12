package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

public class NoAtkBuff extends BaseBuffer{
	public NoAtkBuff(long endFrame) {
		super(UnitBufferType.NoAtkBuff,endFrame,BuffKind.DeBuff);
	}
	
	@Override
	public boolean isCanReplace(Frame frame, BaseBuffer newBuff) {
		return newBuff.getEndFrame() > getEndFrame();
	}
}
