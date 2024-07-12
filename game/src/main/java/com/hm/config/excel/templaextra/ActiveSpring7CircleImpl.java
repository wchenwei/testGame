package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSpring7CircleTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_spring7_circle")
public class ActiveSpring7CircleImpl extends ActiveSpring7CircleTemplate {
	
	private List<Items> reward = Lists.newArrayList();
	
	public void init(){
		this.reward = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	
	public List<Items> getRewards(){
		return this.reward.stream().map(t ->t.clone()).collect(Collectors.toList());
	}
}
