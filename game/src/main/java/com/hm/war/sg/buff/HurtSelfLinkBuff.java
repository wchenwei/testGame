package com.hm.war.sg.buff;

/**
 * @Description: 致命连接buf-队友伤害计算我的血量
 * @author siyunlong  
 * @date 2019年8月13日 上午2:48:10 
 * @version V1.0
 */
public class HurtSelfLinkBuff extends BaseBuffer{
	private int sendId;
	
	public HurtSelfLinkBuff(long endFrame,double value,int sendId) {
		super(UnitBufferType.HurtSelfLink,endFrame,BuffKind.Buff);
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
