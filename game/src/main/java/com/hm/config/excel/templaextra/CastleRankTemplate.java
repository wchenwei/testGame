package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CastleWarRankRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("castle_war_rank_reward")
public class CastleRankTemplate extends CastleWarRankRewardTemplate{
	private List<Items> rewardList;
	
	public void init() {
		this.rewardList = ItemUtils.str2ItemList(getReward_list(), ",", ":");
	}

	public List<Items> getRewardList() {
		return rewardList;
	}
	
	
}
