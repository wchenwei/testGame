package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

/**
 * @Description: 定身buff
 * @author siyunlong  
 * @date 2020年7月22日 下午2:20:15 
 * @version V1.0
 */
public class FixedBodyBuff extends BaseBuffer{
	public FixedBodyBuff(long endFrame) {
		super(UnitBufferType.FixedBodyBuff,endFrame,BuffKind.DeBuff);
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
