package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("level_target")
public class LevelTargetBaseTemplate extends LevelTargetTemplate{
	private List<Items> rewards = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(getReward(), ",", ":");;
	}

	public List<Items> getRewards() {
		return rewards;
	}
	
}
