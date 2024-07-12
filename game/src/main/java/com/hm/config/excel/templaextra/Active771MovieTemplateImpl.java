package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active771MovieTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_771_movie")
public class Active771MovieTemplateImpl extends Active771MovieTemplate {
	private List<Items> rewards = Lists.newArrayList();
	private List<Items> costs = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.costs = ItemUtils.str2ItemList(this.getCost(), ",", ":");
	}
	
	public List<Items> getRewards() {
		return rewards;
	}
	public List<Items> getCosts() {
		return costs;
	}

	public boolean isFit(int id, int choice, int lv) {
		return this.getMovie()==id&&this.getType()==choice&&lv>=this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
	}
	
}
