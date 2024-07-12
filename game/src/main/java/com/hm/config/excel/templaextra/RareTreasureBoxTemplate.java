package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionSweeperBoxTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("mission_sweeper_box")
public class RareTreasureBoxTemplate extends MissionSweeperBoxTemplate{
	private List<Items> rewards = Lists.newArrayList();

	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	
	public List<Items> getRewards(){
		return Lists.newArrayList(rewards);
	}
	
	public List<Items> getRewards(int count){
		return ItemUtils.calItemRateReward(rewards, count);
	}
}
