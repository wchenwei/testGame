package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ItemBattleplaneTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("item_battleplane")
public class ItemBattleplaneTemplateImpl extends ItemBattleplaneTemplate{
	private List<Items> decompseRewards = Lists.newArrayList();
	
	public void init() {
		decompseRewards = ItemUtils.str2ItemList(this.getRecycle(), ",", ":");
	}
	
	public List<Items> getDecompses(int star){
		return ItemUtils.calItemRateReward(decompseRewards, star);
	}

}
