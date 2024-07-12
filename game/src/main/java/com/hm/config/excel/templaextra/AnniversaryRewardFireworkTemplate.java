package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveOneyearRewardFireworkTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_oneyear_reward_firework")
public class AnniversaryRewardFireworkTemplate extends ActiveOneyearRewardFireworkTemplate{
	private List<Drop> drops = Lists.newArrayList();

	public void init(){
		List<String> dropStr = Lists.newArrayList(this.getReward().split(";"));
		this.drops = dropStr.stream().map(t -> new Drop(t)).collect(Collectors.toList());
	}
	
	public List<Items> getRewards(){
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:drops){
			Items item = drop.randomItem();
			if(item != null&&item.getType()!=0&&item.getCount()!=0) {
				dropItems.add(item);
			}
		}
		return dropItems;
	}

	public boolean isFit(int playerLv) {
		return playerLv>=this.getLv_down()&&playerLv<=this.getLv_up();
	}
}
