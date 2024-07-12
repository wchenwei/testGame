package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MysticalShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("mystical_shop")
public class MysteryShopTemplate extends MysticalShopTemplate{
	private List<Items> cost = Lists.newArrayList();
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.cost = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
		this.rewards = ItemUtils.str2ItemList(this.getGoods(), ",", ":");
	}
	
	public List<Items> getCost() {
		return cost;
	}

	public List<Items> getRewards() {
		return rewards;
	}

	public boolean isFit(int lv){
		return lv>=this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
	}
}
