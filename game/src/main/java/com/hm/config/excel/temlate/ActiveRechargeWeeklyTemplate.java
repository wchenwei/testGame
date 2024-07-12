package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_recharge_week")
public class ActiveRechargeWeeklyTemplate extends ActiveRechargeWeekTemplate{
	
	private List<Items> rewards = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",",":");
	}

	public List<Items> getRewards() {
		return rewards;
	}

	public boolean isFit(int lv) {
		return lv>=getPalyer_lv_down()&&lv<=getPalyer_lv_up();
	}

}
