package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveDragonboatFormulaTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_dragonBoat_formula")
public class Active55FormulaImpl extends ActiveDragonboatFormulaTemplate{
	//奖励
	private List<Items> rewards = Lists.newArrayList();
	//消耗
	private List<Items> cost = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getProduct(), ",", ":");
		this.cost = ItemUtils.str2ItemList(this.getFormula(), ",", ":");
	}
	public List<Items> getRewards(int count) {
		List<Items> tempReward = rewards.stream().map(t -> t.clone()).collect(Collectors.toList());
		return ItemUtils.calItemRateReward(tempReward, count);
	}
	public List<Items> getCost(int rate) {
		List<Items> tempCost = cost.stream().map(t -> t.clone()).collect(Collectors.toList());
		return ItemUtils.calItemRateReward(tempCost, rate);
	}
	
	public boolean checkLv(int lv, int stage) {
		return lv>=this.getServer_level_low() && lv<= this.getServer_level_high() && this.getStage()==stage;
	}

}
