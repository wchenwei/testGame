package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.KfPkRewardSeasonTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("kf_pk_reward_season")
public class KfLevelRewardSeasonTemplate extends KfPkRewardSeasonTemplate{
	private List<Items> rewardList;
	
	public void init() {
		this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
	}

	public List<Items> getRewardList() {
		return rewardList;
	}
}
