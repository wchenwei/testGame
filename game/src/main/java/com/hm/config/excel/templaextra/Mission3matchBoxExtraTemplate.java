package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Mission3matchBoxTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("mission_3match_box")
public class Mission3matchBoxExtraTemplate extends Mission3matchBoxTemplate {
	private List<Items> rewards = Lists.newArrayList();

	public void init(){
		this.rewards = ItemUtils.str2DefaultItemImmutableList(this.getReward());
	}

	public List<Items> getRewards(){
		return this.rewards;
	}
}
