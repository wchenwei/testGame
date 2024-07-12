package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.ActiveSevenLoginRewardTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
@FileConfig("active_seven_login_reward")
public class ActivitySevenLoginWelfareTemplate extends
		ActiveSevenLoginRewardTemplate {
	private List<Items> rewards = Lists.newArrayList();


	@ConfigInit
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}

	public List<Items> getRewards() {
		return rewards;
	}
}