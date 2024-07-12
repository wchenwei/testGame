package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerHeadTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
@FileConfig("player_head")
public class PlayerHeadExtraTemplate extends PlayerHeadTemplate {
	private Items costItem;
	public void init(){
		this.costItem = ItemUtils.str2Item(this.getCost(), ":");
	}
	public Items getCostItem() {
		return costItem;
	}

}
