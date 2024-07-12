package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WarCraftSkillUpgradeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("war_craft_skill_upgrade")
public class WarCraftSkillUpgradeTemplateImpl extends WarCraftSkillUpgradeTemplate{
	private List<Items> costs = Lists.newArrayList();
	
	public void init(){
		this.costs = ItemUtils.str2ItemList(this.getCost(), ",", ":");
	}
	
	public List<Items> getCosts() {
		return costs;
	}
}
