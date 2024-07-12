package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

/**
 * @Description: 无敌buff
 * @author siyunlong  
 * @date 2018年10月9日 下午3:02:16 
 * @version V1.0
 */
public class InvincibleBuff extends BaseBuffer{
	public InvincibleBuff(long endFrame) {
		super(UnitBufferType.InvincibleBuff,endFrame,BuffKind.Buff);
	}
	
	@Override
	public boolean isCanReplace(Frame frame, BaseBuffer newBuff) {
		return newBuff.getEndFrame() > getEndFrame();
	}
}
