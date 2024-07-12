package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ShopGoodsTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;
@FileConfig("shop_goods")
public class ShopGoodsExtraTemplate extends ShopGoodsTemplate {
	private Items reward;
	private List<Items> cost =Lists.newArrayList();
	public void init(){
		this.reward = ItemUtils.str2Item(this.getItem(), ":");
		this.cost = ItemUtils.str2ItemList(this.getPrice(),",", ":");
	}
	public Items getReward() {
		return reward.clone();
	}
	public List<Items> getCost() {
		return cost.stream().map(e -> e.clone()).collect(Collectors.toList());
	}
	
}
