package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CityWarSkillBoxTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("city_war_skill_box")
public class CityWarSkillBoxTemplateImpl extends CityWarSkillBoxTemplate {

	private List<Items> rewardItems = Lists.newArrayList();
	
	public void init(){
		this.rewardItems = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	
	public List<Items> getRewardItems() {
		return rewardItems.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
}
