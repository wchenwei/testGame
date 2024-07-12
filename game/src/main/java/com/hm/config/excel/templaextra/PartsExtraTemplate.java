package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PartsTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("parts")
public class PartsExtraTemplate extends PartsTemplate{
	private List<Items> sellReward = Lists.newArrayList();
	public void init(){
		this.sellReward = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
	}
	public List<Items> getSellReward() {
		return sellReward;
	}
	
	
}
