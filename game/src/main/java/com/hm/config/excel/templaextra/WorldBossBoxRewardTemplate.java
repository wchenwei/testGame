package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WorldBossRandomRewardTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("world_boss_random_reward")
public class WorldBossBoxRewardTemplate extends WorldBossRandomRewardTemplate{
	private List<Drop> drops = Lists.newArrayList();
	public void init(){
		List<String> dropStr = Lists.newArrayList(this.getRandom_reward().split(";"));
		this.drops = dropStr.stream().map(t -> new Drop(t)).collect(Collectors.toList());
	}
	
	public List<Items> getRewards(){
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:drops){
			Items item = drop.randomItem();
			if(item != null) {
				dropItems.add(item);
			}
		}
		return dropItems;
	}
}
