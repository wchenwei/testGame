package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

/**
 * @Description:伤害大于血量80% 影响buff -收到伤害增加/减少
 * @author siyunlong  
 * @date 2018年10月18日 上午11:48:24 
 * @version V1.0
 */
public class HurtHp80EffectBuff extends BaseBuffer{
	
	public HurtHp80EffectBuff(long endFrame,double value) {
		super(UnitBufferType.HurtHp80EffectBuff,endFrame,BuffKind.None);
		setValue(value);
	}

	@Override
	public boolean isCanReplace(Frame frame,BaseBuffer buff) {
		return this.getFuncId() == buff.getFuncId();
	}
}