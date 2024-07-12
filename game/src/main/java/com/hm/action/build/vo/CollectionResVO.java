package com.hm.action.build.vo;

import com.hm.model.player.CurrencyKind;

public class CollectionResVO {
	private CurrencyKind currencyKind;//资产类型
	private long curYield;
	
	public CollectionResVO(CurrencyKind currencyKind, long curYield) {
		super();
		this.currencyKind = currencyKind;
		this.curYield = curYield;
	}
	public CurrencyKind getCurrencyKind() {
		return currencyKind;
	}
	public void setCurrencyKind(CurrencyKind currencyKind) {
		this.currencyKind = currencyKind;
	}
	public long getCurYield() {
		return curYield;
	}
	public void setCurYield(long curYield) {
		this.curYield = curYield;
	}
	
	
	
}
