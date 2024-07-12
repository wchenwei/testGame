package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CarSkillCostTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

@FileConfig("car_skill_cost")
public class CarSkillTemplate extends CarSkillCostTemplate {
	private Items costItems = new Items(); 
	private Items totalCostItems = new Items(); 
	
	public void init(){
		this.costItems = ItemUtils.str2Item(getCost(), ":");
		this.totalCostItems = ItemUtils.str2Item(getCost_total(), ":");
	}

	public Items getCostItems() {
		return costItems;
	}

	public Items getTotalCostItems() {
		return totalCostItems;
	}
	
	
	
}
