package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;

import java.util.List;

@FileConfig("gift")
public class GiftPackageTemplate extends GiftTemplate{
	private List<Items> itemList;

	@ConfigInit
	public void init() {
		List<Items> itemList = Lists.newArrayList();
		String[] list = getDrop_type().split(",");
		for (String s : list) {
			String[] its = s.split(":");
			int itemType = Integer.parseInt(its[0]);
			int itemId = Integer.parseInt(its[1]);
			int count = Integer.parseInt(its[2]);
			Items item = new Items(itemId);
			item.setCount(count);
			item.setItemType(itemType);
			itemList.add(item);
		}
		this.itemList = itemList;
	}

	public List<Items> getItemList() {
		return itemList;
	}
	
	
}
