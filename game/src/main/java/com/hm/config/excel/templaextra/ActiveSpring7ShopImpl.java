package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSpring7ShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_spring7_shop")
public class ActiveSpring7ShopImpl extends ActiveSpring7ShopTemplate {
	private List<Items> reward = Lists.newArrayList();
	private List<Items> cost = Lists.newArrayList();
	
	public void init(){
		this.reward = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.cost = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
	}
	
	public List<Items> getRewards(){
		return this.reward.stream().map(t ->t.clone()).collect(Collectors.toList());
	}
	public List<Items> getCost() {
		return this.cost.stream().map(t ->t.clone()).collect(Collectors.toList());
	}
}
