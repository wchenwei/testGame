package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveChirstClickRewardTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_chirst_click_reward")
public class ActivityChirstClickRewardTemplate extends ActiveChirstClickRewardTemplate{
	private Drop drop ;
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.drop = new Drop(this.getReward());
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	public Drop getDrop() {
		return drop;
	}
	
	public Items randomReward(){
		return this.drop.randomItem();
	}
	public boolean isFit(int lv,int type) {
		return this.getType()==type&&lv>=this.getServer_lv_down()&&lv<=this.getServer_lv_up();
	}
	
	public List<Items> getRewards(){
		return rewards;
	}
}
