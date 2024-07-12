package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_recharge_once")
public class ActiveRechargeSingleTemplate extends ActiveRechargeOnceTemplate {
private List<Items> rewards = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",",":");
	}

	public List<Items> getRewards() {
		return rewards;
	}

	public boolean isFit(int lv) {
		return lv>=getPlayer_lv_down()&&lv<=getPlayer_lv_up();
	}
	
}
