package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active120RechargeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_120_recharge")
public class Active120RechargeTemplateImpl extends Active120RechargeTemplate {
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
