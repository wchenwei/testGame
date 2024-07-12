package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CityWarSkillUpgradeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;
@FileConfig("city_war_skill_upgrade")
public class CityWarSkillUpgradeTemplateImpl extends CityWarSkillUpgradeTemplate {

	private List<Items> costItems = Lists.newArrayList();
	
	public void init(){
		this.costItems = ItemUtils.str2ItemList(this.getUpgrade_cost(), ",", ":");
	}

	public List<Items> getCostItems() {
		return costItems.stream().map(t -> t.clone()).collect(Collectors.toList());
	}

}
