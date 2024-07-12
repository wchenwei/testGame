package com.hm.war.sg.buff;

/**
 * @Description: 所有能量减少百分比
 * @author siyunlong  
 * @date 2019年8月13日 上午2:48:10 
 * @version V1.0
 */
public class ReduceMpBuff extends BaseBuffer{
	private int sendId;
	
	public ReduceMpBuff(long endFrame,double value,int sendId) {
		super(UnitBufferType.ReduceMpBuff,endFrame,BuffKind.DeBuff);
		setValue(value);
		this.sendId = sendId;
	}
	
	@Override
	public boolean isOverlying() {
		return true;
	}
	
	@Override
	public int getBuffSendId() {
		return sendId;
	}
}
