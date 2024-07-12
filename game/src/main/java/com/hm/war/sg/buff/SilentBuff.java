package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

public class SilentBuff extends BaseBuffer{
	public SilentBuff(long endFrame) {
		super(UnitBufferType.SilentBuff,endFrame,BuffKind.DeBuff);
	}
	
	@Override
	public boolean isCleanSkillPre() {
		return true;
	}
	
	@Override
	public boolean isCanReplace(Frame frame,BaseBuffer newBuff) {
		return newBuff.getEndFrame() > getEndFrame();
	}
}
