package com.hm.war.sg.buff;

/**
 * @Description: 标记buf
 * @author siyunlong  
 * @date 2018年10月9日 下午3:02:16 
 * @version V1.0
 */
public class MarkBuff extends BaseBuffer{
	private int unitId;
	
	public MarkBuff(long endFrame,int unitId) {
		super(UnitBufferType.MarkBuff,endFrame,BuffKind.DeBuff);
		this.unitId = unitId;
	}
	
	@Override
	public int getBuffSendId() {
		return unitId;
	}
	
	@Override
	public boolean isOverlying() {
		return true;
	}
}
