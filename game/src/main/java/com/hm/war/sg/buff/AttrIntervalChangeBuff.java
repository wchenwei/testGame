package com.hm.war.sg.buff;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

/**
 * 
 * @Description: 间隔n帧增加属性数值buff
 * @author siyunlong  
 * @date 2018年11月17日 下午2:02:06 
 * @version V1.0
 */
public class AttrIntervalChangeBuff extends AttrBuff{
	private long nextAddValueInterval;//下次增加帧
	private long intervalFrame;
	private double addValue;//每次增加
	
	public AttrIntervalChangeBuff(long endFrame,TankAttrType attrType,double value,int sendId,int unitId,int funcId,BuffKind buffKind) {
		super(endFrame, attrType, value, sendId,unitId, funcId, buffKind);
		this.addValue = value;
	}

	public void setNextAddValueInterval(Frame frame,long intervalFrame) {
		this.nextAddValueInterval = frame.getId()+intervalFrame;
		this.intervalFrame = intervalFrame;
	}
	
	@Override
	public void doEffectBuff(Frame frame,Unit unit) {
		if(frame.getId() >= nextAddValueInterval) {
			this.nextAddValueInterval += intervalFrame;
			setValue(getValue() + addValue);
		}
	}
}
