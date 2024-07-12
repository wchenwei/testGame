package com.hm.libcore.util.lv;

public class LvValueTest implements ILvValue{
	public int lv;
	public long exp;
	
	public LvValueTest(int lv, long exp) {
		super();
		this.lv = lv;
		this.exp = exp;
	}

	@Override
	public int getLv() {
		return this.lv;
	}

	@Override
	public long getExp() {
		return exp;
	}
	
	
}
