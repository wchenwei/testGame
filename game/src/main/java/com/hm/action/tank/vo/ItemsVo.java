package com.hm.action.tank.vo;

import com.hm.model.item.Items;

public class ItemsVo extends Items{
	private boolean convert = false;

	public ItemsVo(Items items, boolean convert) {
		this.setId(items.getId());
		this.setCount(items.getCount());
		this.setItemType(items.getItemType());
		this.convert = convert;
	}
	public boolean isConvert() {
		return convert;
	}
	public void setConvert(boolean convert) {
		this.convert = convert;
	}
}
