package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active77ShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_77_shop")
public class ActiveValentineShopTemplate extends Active77ShopTemplate{
	private Items reward;
	private List<Items> costs = Lists.newArrayList();
	
	public void init(){
		this.reward = ItemUtils.str2Item(this.getGoods(), ":");
		this.costs = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
	}

	public Items getReward() {
		return reward;
	}

	public List<Items> getCosts() {
		return costs;
	}
	
	
}
