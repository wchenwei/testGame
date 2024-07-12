package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSignTotalTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_sign_total")
public class ActivitySignTotalTemplate extends ActiveSignTotalTemplate{
	private List<Items> itemList = Lists.newArrayList();
	
	public void init() {
		this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":");
	}
	
	public List<Items> getItemList() {
		return itemList;
	}
}
