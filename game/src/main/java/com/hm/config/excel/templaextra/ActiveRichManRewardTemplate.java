package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRichmanRankrewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_richman_rankreward")
public class ActiveRichManRewardTemplate extends ActiveRichmanRankrewardTemplate{
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	public List<Items> getRewards() {
		return rewards;
	}
}
