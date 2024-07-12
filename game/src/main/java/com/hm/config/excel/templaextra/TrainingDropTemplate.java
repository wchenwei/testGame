package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.BuildingTrainingDropTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("building_training_drop")
public class TrainingDropTemplate extends BuildingTrainingDropTemplate{
	private List<Drop> drops = Lists.newArrayList();
	private List<Drop> drops2 = Lists.newArrayList();
	public void init() {
		List<String> dropStr = Lists.newArrayList(this.getReward().split(";"));
		this.drops = dropStr.stream().map(t -> new Drop(t)).collect(Collectors.toList());
		List<String> dropStr2 = Lists.newArrayList(this.getReward_buff().split(";"));
		this.drops2 = dropStr2.stream().map(t -> new Drop(t)).collect(Collectors.toList());
	}
	
	public List<Items> getRewards(int state){
		List<Items> dropItems = Lists.newArrayList();
		List<Drop> d = state==1?drops2:drops;
		for(Drop drop:d){
			Items item = drop.randomItem();
			if(item != null&&item.getType()!=0&&item.getCount()!=0) {
				dropItems.add(item);
			}
		}
		return dropItems;
	}
}
