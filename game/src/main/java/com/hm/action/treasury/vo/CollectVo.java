package com.hm.action.treasury.vo;

import com.hm.model.item.Items;

public class CollectVo {
	private Items item;
	private int critical;
	public Items getItem() {
		return item;
	}
	
	public CollectVo(Items item, int critical) {
		super();
		this.item = item;
		this.critical = critical;
	}

	public void setItem(Items item) {
		this.item = item;
	}
	public int getCritical() {
		return critical;
	}
	public void setCritical(int critical) {
		this.critical = critical;
	}
	

}
