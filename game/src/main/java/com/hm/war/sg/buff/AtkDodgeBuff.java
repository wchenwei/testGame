package com.hm.war.sg.buff;

/**
 * @Description: 普攻missbuff
 * @author siyunlong  
 * @date 2019年8月13日 上午2:48:10 
 * @version V1.0
 */
public class AtkDodgeBuff extends BaseBuffer{
	private int sendId;
	
	public AtkDodgeBuff(long endFrame,double value,int sendId) {
		super(UnitBufferType.AtkDodgeBuff,endFrame,BuffKind.DeBuff);
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
