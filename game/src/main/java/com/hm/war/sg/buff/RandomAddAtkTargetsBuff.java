package com.hm.war.sg.buff;

import com.hm.util.RandomUtils;

public class RandomAddAtkTargetsBuff extends BaseBuffer{
	private double rate;
	private int count;//随机个数
	
	public RandomAddAtkTargetsBuff(long endFrame,double value,Object confValue) {
		super(UnitBufferType.RandomAddAtkTargets,endFrame,BuffKind.Buff);
		this.rate = value;
		this.count = (int)confValue;
	}
	
	public int getAddCount() {
		return RandomUtils.randomIsRate(rate)?count:0;
	}
}
