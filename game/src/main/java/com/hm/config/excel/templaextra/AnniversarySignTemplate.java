package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveOneyearSignTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_oneyear_sign")
public class AnniversarySignTemplate extends ActiveOneyearSignTemplate{
	private List<Items> signRewards = Lists.newArrayList();
	private List<Items> rechargeRewards = Lists.newArrayList();
	
	public void init(){
		this.signRewards = ItemUtils.str2ItemList(this.getReward_sign(), ",", ":");
		this.rechargeRewards = ItemUtils.str2ItemList(this.getReward_recharge(), ",", ":");
	}

	public List<Items> getSignRewards() {
		return signRewards;
	}

	public List<Items> getRechargeRewards() {
		return rechargeRewards;
	}

	public boolean isFit(int lv, int days) {
		return lv>=this.getLv_down()&&lv<=this.getLv_up()&&days>=this.getDay();
	}
	
	
}
