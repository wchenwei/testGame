package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.DutyRoadRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("duty_road_reward")
public class DutyRoadRewardExtraTemplate extends DutyRoadRewardTemplate{
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	public List<Items> getRewards(){
		return this.rewards.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	
}
