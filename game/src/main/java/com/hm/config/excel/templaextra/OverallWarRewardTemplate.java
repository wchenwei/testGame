package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TotalWarRewardTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("total_war_reward")
public class OverallWarRewardTemplate extends TotalWarRewardTemplate{
	private List<Items> rewards =Lists.newArrayList();
	//翻牌掉落
	private List<Drop> flops = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		List<String> flopStr = Lists.newArrayList(this.getTurn_card().split(";"));
		this.flops = flopStr.stream().map(t -> new Drop(t)).collect(Collectors.toList());
	}

	public List<Items> getRewards() {
		return rewards;
	}
	
	public List<Drop> getFlops() {
		return flops;
	}
	
	
}
