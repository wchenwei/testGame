package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

public class StunBuff extends BaseBuffer{
	public StunBuff(long endFrame) {
		super(UnitBufferType.StunBuff,endFrame,BuffKind.DeBuff);
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
