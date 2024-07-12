package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MasteryCostTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("mastery_cost")
public class MasteryLvUpCostTemplate extends MasteryCostTemplate{
	private List<Items> costItems = Lists.newArrayList();
	public void init(){
		this.costItems = ItemUtils.str2ItemList(this.getCost(), ",", ":");
	}
	public List<Items> getCostItems() {
		return costItems;
	}
	
}
