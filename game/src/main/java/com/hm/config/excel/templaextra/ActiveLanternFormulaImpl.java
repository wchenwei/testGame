package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveLanternFormulaTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_lantern_formula")
public class ActiveLanternFormulaImpl extends ActiveLanternFormulaTemplate {
	//奖励
	private List<Items> rewards = Lists.newArrayList();
	//合成消耗
	private List<Items> cost = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getProduct(), ",", ":");
		this.cost = ItemUtils.str2ItemList(this.getFormula(), ",", ":");
	}
	public List<Items> getRewards() {
		return rewards.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	public List<Items> getCost() {
		return cost.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	
	public boolean checkLv(int lv) {
		return lv>=this.getServer_level_low() && lv<= this.getServer_level_high();
	}
}
