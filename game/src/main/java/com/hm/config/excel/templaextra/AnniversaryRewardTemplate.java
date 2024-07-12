package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveOneyearRewardTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_oneyear_reward")
public class AnniversaryRewardTemplate extends ActiveOneyearRewardTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private Drop drop;
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.drop = new Drop(this.getReward_random());
	}
	
	public List<Items> getRewards(){
		return rewards;
	}
	
	public List<Items> getRandomReward(int count){
		List<Items> rewards = Lists.newArrayList();
		for(int i=1;i<=count;i++){
			rewards.add(drop.randomItem());
		}
		return rewards;
	}	
	
	public boolean isFit(int layer,int lv){
		return lv>=this.getLv_down()&&lv<=this.getLv_up()&&this.getLevel()==layer;
	}
}