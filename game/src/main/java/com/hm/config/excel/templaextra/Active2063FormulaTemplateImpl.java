package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active2063FormulaTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_2063_formula")
public class Active2063FormulaTemplateImpl extends Active2063FormulaTemplate {
	//奖励
	private List<Items> rewards = Lists.newArrayList();
	//消耗
	private List<Items> cost = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2DefaultItemImmutableList(this.getProduct());
		this.cost = ItemUtils.str2DefaultItemImmutableList(this.getFormula());
	}
	public List<Items> getRewards(int count) {
		return ItemUtils.calItemRateReward(rewards, count);
	}
	public List<Items> getCost(int rate) {
		return ItemUtils.calItemRateReward(cost, rate);
	}
	
	public boolean checkLv(int lv) {
		return lv>=this.getServer_level_low() && lv<= this.getServer_level_high();
	}

}
