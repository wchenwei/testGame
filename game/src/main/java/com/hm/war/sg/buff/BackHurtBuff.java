package com.hm.war.sg.buff;

public class BackHurtBuff extends BaseBuffer{
	public BackHurtBuff(long endFrame,double value) {
		super(UnitBufferType.BackHurtBuff,endFrame,BuffKind.Buff);
		setValue(value);
	}
	
	@Override
	public boolean isOverlying() {
		return true;
	}
}
