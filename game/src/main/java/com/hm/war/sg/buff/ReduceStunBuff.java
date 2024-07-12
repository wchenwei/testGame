package com.hm.war.sg.buff;

/**
 * @Description: 减少眩晕buff
 * @author siyunlong  
 * @date 2019年8月13日 上午2:48:10 
 * @version V1.0
 */
public class ReduceStunBuff extends BaseBuffer{
	private int sendId;
	
	public ReduceStunBuff(long endFrame,double value,int sendId) {
		super(UnitBufferType.ReduceStunBuff,endFrame,BuffKind.Buff);
		setValue(value);
		this.sendId = sendId;
	}
	
	@Override
	public int getBuffSendId() {
		return sendId;
	}
}
