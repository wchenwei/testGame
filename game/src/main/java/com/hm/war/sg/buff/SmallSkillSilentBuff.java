package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

public class SmallSkillSilentBuff extends BaseBuffer{
	public SmallSkillSilentBuff(long endFrame) {
		super(UnitBufferType.SmallSkillSilentBuff,endFrame,BuffKind.DeBuff);
	}
	
	@Override
	public boolean isCanReplace(Frame frame,BaseBuffer newBuff) {
		return newBuff.getEndFrame() > getEndFrame();
	}
}
