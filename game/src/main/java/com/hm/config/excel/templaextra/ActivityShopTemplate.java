package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_shop")
public class ActivityShopTemplate extends ActiveShopTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Items> cost = Lists.newArrayList();
	private List<Items> costOther = Lists.newArrayList();

	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getGoods(), ",", ":");
		this.cost = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
		this.costOther = ItemUtils.str2ItemList(this.getPrice_other(), ",", ":");
	}

	public List<Items> getRewards() {
		return rewards;
	}

	public List<Items> getCost() {
		return cost;
	}
	
	public boolean isFit(int lv){
		return lv>= this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
	}

	public List<Items> getCostOther() {
		return costOther;
	}
}
