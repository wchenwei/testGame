package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

/**
 * @Description: 不死buff
 * @author siyunlong  
 * @date 2018年10月9日 下午3:02:16 
 * @version V1.0
 */
public class NoDeathBuff extends BaseBuffer{
	public NoDeathBuff(long endFrame) {
		super(UnitBufferType.NoDeath,endFrame,BuffKind.Buff);
	}
	
	@Override
	public boolean isCanReplace(Frame frame, BaseBuffer newBuff) {
		return newBuff.getEndFrame() > getEndFrame();
	}
}
