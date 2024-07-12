package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveChirstDressTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_chirst_dress")
public class ActivityChirstDressTemplate extends ActiveChirstDressTemplate{
	private List<Items> costs = Lists.newArrayList();
	
	public void init(){
		this.costs = ItemUtils.str2ItemList(this.getCost(), ",", ":");
	}

	public List<Items> getCosts() {
		return costs;
	}
	
}
