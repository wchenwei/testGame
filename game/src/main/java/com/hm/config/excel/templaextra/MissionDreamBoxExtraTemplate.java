package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionDreamBoxTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("mission_dream_box")
public class MissionDreamBoxExtraTemplate extends MissionDreamBoxTemplate{
	private List<Items> rewards = Lists.newArrayList();
	
	public void init() {
		this.rewards=ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	
	public List<Items> getRewards(){
		return rewards;
	}
}
