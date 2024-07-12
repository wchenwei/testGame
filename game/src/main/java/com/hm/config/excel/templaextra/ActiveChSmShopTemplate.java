package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRechargeOfflineTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_recharge_offline")
public class ActiveChSmShopTemplate extends ActiveRechargeOfflineTemplate{
	//奖励
	private List<Items> itemList = Lists.newArrayList();
	
	public void init() {
		this.itemList = ItemUtils.str2ItemList(this.getGoods(), ",", ":");
	}

	public List<Items> getItemList() {
		return itemList;
	}
	
	
}
