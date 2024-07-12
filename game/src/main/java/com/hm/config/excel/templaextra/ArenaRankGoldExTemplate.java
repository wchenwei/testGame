package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ArenaRankGoldTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("arena_rank_gold")
public class ArenaRankGoldExTemplate extends ArenaRankGoldTemplate{
	private List<Items> itemList = Lists.newArrayList(); 
	
	public void init(){
		itemList = ItemUtils.str2ItemList(getGold(), ",", ":");
	}

	public List<Items> getItemList() {
		return itemList;
	}

	
}
