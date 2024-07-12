package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveOneyearCakeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

@FileConfig("active_oneyear_cake")
public class AnniversaryCakeTemplate extends ActiveOneyearCakeTemplate{
	private Items cost;
	
	public void init(){
		this.cost = ItemUtils.str2Item(this.getFormula(), ":");
	}
	
	public Items getCost(){
		return cost.clone();
	}
}
