package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.BuildingEnhanceBreakTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("building_enhance_break")
public class TankStrengthBreakTemplate extends BuildingEnhanceBreakTemplate{
	private List<Items> costs = Lists.newArrayList();
	public void init() {
		this.costs = ItemUtils.str2ItemList(this.getBreak_cost(), ",", ":");
	}
	public List<Items> getCosts() {
		return costs;
	}
	
	
}
