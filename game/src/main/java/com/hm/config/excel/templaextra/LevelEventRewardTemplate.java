package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionLevelRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("mission_level_reward")
public class LevelEventRewardTemplate extends MissionLevelRewardTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private int rewardType;
	private int needNum;
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.rewardType = Integer.parseInt(this.getReward_case().split(":")[0]);
		this.needNum = Integer.parseInt(this.getReward_case().split(":")[1]);
	}

	public List<Items> getRewards() {
		return rewards;
	}

	public int getRewardType() {
		return rewardType;
	}

	public int getNeedNum() {
		return needNum;
	}
	
	
}
