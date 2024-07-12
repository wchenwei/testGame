package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active771RewardOnceTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_771_reward_once")
public class Active771RewardOnceTemplateImpl extends Active771RewardOnceTemplate {
	List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public boolean isFit(int type,int lv) {
		return type==this.getType()&&lv>=this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
	}
	
	
}
