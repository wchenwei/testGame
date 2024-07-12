package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveWeekRewardTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_week_reward")
public class ActivityWeekRewardTemplate extends ActiveWeekRewardTemplate {
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2DefaultItemImmutableList(this.getReward_back());
	}

	public List<Items> getRewards(){
		return rewards;
	}

	public boolean isFit(BasePlayer player, int taskLv){
		return taskLv>=this.getNeed_level()&&player.getPlayerVipInfo().getVipLv()>=this.getNeed_vip();
	}

}
