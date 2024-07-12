package com.hm.war.sg.buff;

public class SuckBloodBuff extends BaseBuffer{
	public SuckBloodBuff(long endFrame,double value) {
		super(UnitBufferType.AtkSuckBlood,endFrame,BuffKind.Buff);
		setValue(value);
	}
}

