package com.hm.war.sg.buff;

public class AddAtkTargetsBuff extends BaseBuffer{
	private int count;
	
	public AddAtkTargetsBuff(long endFrame,double value) {
		super(UnitBufferType.AddAtkTargets,endFrame,BuffKind.Buff);
		this.count = (int)value;
	}

	public int getAddCount() {
		return count;
	}
	
	
}
