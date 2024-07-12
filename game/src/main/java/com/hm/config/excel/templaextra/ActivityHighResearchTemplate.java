package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveHighResearchTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_high_research")
public class ActivityHighResearchTemplate extends ActiveHighResearchTemplate{
	private List<Items> itemList = Lists.newArrayList(); 
	
	public void init(){
		this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":"); 
	}

	public List<Items> getItemList() {
		return itemList;
	}
	
}
