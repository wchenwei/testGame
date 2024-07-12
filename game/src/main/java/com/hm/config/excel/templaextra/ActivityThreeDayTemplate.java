package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.ActiveThreeDayTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_three_day")
public class ActivityThreeDayTemplate extends ActiveThreeDayTemplate {
	private List<Items> itemList = Lists.newArrayList(); 
	public void init(){
		this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":"); 
	}

	public List<Items> getItemList() {
		return itemList;
	}




}
