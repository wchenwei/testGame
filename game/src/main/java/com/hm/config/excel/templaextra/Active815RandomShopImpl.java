package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active815RandomShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;
@FileConfig("active_815_random_shop")
public class Active815RandomShopImpl extends Active815RandomShopTemplate{
	
	/*
	 * 
	购买类型	 type
	1	人民币
	2	金砖
	
	限购类型	buy_type
	1	每天限购1次
	2	每次限购1次
	
	*/


	//奖励
	private List<Items> rewards = Lists.newArrayList();
	//购买礼包消耗
	private List<Items> cost = Lists.newArrayList();
	
	public void init(){
		if(this.getType()==2) {
			this.rewards = ItemUtils.str2ItemList(this.getGoods(), ",", ":");
			this.cost = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
		}
	}

	public List<Items> getRewards() {
		return rewards.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	public boolean checkLv(int lv) {
		return lv>=this.getServer_level_low() && lv<= this.getServer_level_high();
	}

	public List<Items> getCost() {
		return cost.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
}
