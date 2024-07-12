package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ShopFortessTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("shop_fortess")
public class FortessShopItemTemplate extends ShopFortessTemplate{
	private Items spendItem;
	private List<Items> rewardItem;
	
	public void init() {
		this.spendItem = ItemUtils.str2Item(getCost(), ":");
		this.rewardItem = Lists.newArrayList(ItemUtils.str2Item(getSale(), ":"));
	}

	public Items getSpendItem() {
		return spendItem;
	}

	public List<Items> getRewardItem() {
		return rewardItem;
	}
	
	
}
