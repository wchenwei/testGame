package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ViceSoldierLevelTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("vice_soldier_level")
public class TankSoldierLevelTemplate extends ViceSoldierLevelTemplate{
	private List<Items> costs = Lists.newArrayList();
	
	public void init(){
		this.costs = ItemUtils.str2ItemList(this.getUpgrade_cost(), ",", ":");
	}
	
	public List<Items> getCosts(){
		return costs;
	}
}
