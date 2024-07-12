package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: 护盾buf
 * @author siyunlong  
 * @date 2018年10月9日 下午3:02:16 
 * @version V1.0
 */
public class ShieldBuff extends BaseBuffer{
	public long shieldValue;//护盾值
	
	public ShieldBuff(long endFrame,long value) {
		super(UnitBufferType.ShieldBuff,endFrame,BuffKind.None);
		this.shieldValue = value;
	}

	public long getShieldValue() {
		return shieldValue;
	}
	
	/**
	 * 处理护盾伤害
	 * @param hurt
	 * @return
	 */
	public long doHurt(Frame frame, Unit unit, long hurt) {
		if(this.shieldValue >= hurt) {
			this.shieldValue -= hurt;
			return 0;
		}else{
			long lastHurt = hurt - this.shieldValue;
			this.shieldValue = 0;
			doRemoveAction(frame, unit);
			return lastHurt;
		}
	}
	
	@Override
	public boolean isCanReplace(Frame frame,BaseBuffer buff) {
		ShieldBuff newBuff = (ShieldBuff)buff;
		return newBuff.getShieldValue() > getShieldValue() ||
				getEndFrame() - frame.getId() < 10;
	}
}
