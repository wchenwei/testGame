package com.hm.war.sg.buff;

import com.hm.util.RandomUtils;
import com.hm.war.sg.Frame;

/**
 * 概率buff
 */
public class RateBuff extends BaseBuffer{
	
	public RateBuff(UnitBufferType buffType,long endFrame,double value) {
		super(buffType,endFrame,BuffKind.Buff);
		setValue(value);
	}
	
	@Override
	public boolean isCanReplace(Frame frame, BaseBuffer newBuff) {
		if(newBuff.getEndFrame() == -1) {
			return true;
		}
		if(getEndFrame() == -1) {
			return false;
		}
		return newBuff.getEndFrame() > getEndFrame();
	}
	//是否触发
	public boolean isTrigger() {
		return RandomUtils.randomIsRate(getValue());
	}


	@Override
	public boolean isOverlying() {
		return true;
	}
}
