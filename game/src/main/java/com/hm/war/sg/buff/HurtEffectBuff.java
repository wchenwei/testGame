package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

/**
 * @Description:伤害影响buff -收到伤害增加/减少
 * @author siyunlong  
 * @date 2018年10月18日 上午11:48:24 
 * @version V1.0
 */
public class HurtEffectBuff extends BaseBuffer{
	
	public HurtEffectBuff(long endFrame,double value) {
		super(UnitBufferType.HurtEffectBuff,endFrame,BuffKind.None);
		setValue(value);
	}

	@Override
	public boolean isCanReplace(Frame frame,BaseBuffer buff) {
		return this.getFuncId() == buff.getFuncId();
	}


	@Override
	public boolean isOverlying() {
		return true;
	}
}