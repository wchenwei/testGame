package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WarPlayerRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("war_player_reward")
public class PlayerGuildWarTaskRewardTemplate extends WarPlayerRewardTemplate{
	private List<Items> itemList = Lists.newArrayList();
	
	public void init(){
		this.itemList = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}

	public List<Items> getItemList() {
		return itemList;
	}

}
