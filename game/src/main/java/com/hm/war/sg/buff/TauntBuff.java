package com.hm.war.sg.buff;

/**
 * @Description: 嘲讽buf
 * @author siyunlong  
 * @date 2018年10月9日 下午3:02:16 
 * @version V1.0
 */
public class TauntBuff extends BaseBuffer{
	private int unitId;
	
	public TauntBuff(long endFrame,int unitId) {
		super(UnitBufferType.TauntBuff,endFrame,BuffKind.DeBuff);
		this.unitId = unitId;
	}
	
	@Override
	public int getBuffSendId() {
		return unitId;
	}
	
	@Override
	public boolean isCleanSkillPre() {
		return true;
	}
}
