package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveChirstProgressTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_chirst_progress")
public class ActivityChirstProgressTemplate extends ActiveChirstProgressTemplate{
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public boolean isFit(int playerLv) {
		return playerLv>=this.getPlayer_lv_down()&&playerLv<=this.getPlayer_lv_up();
	}
	
}
