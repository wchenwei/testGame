package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionSingleAttackTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("mission_single_attack")
public class RaidBattleTemplate extends MissionSingleAttackTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Drop> sweepDrops = Lists.newArrayList();

	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getBox_reward(), ",", ":");
		List<String> dropStr = Lists.newArrayList(this.getReward().split(";"));
		this.sweepDrops = dropStr.stream().map(t -> new Drop(t)).collect(Collectors.toList());
	}
	
	public List<Items> getRewards() {
		return rewards;
	}
	public List<Items> getSweepRewards(){
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:sweepDrops){
			Items item = drop.randomItem();
			if(item != null) {
				dropItems.add(item);
			}
		}
		return dropItems;
	}
	
}
