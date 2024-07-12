package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ItemGuildWeaponTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("item_guild_weapon")
public class ItemGuildWeaponExtraTemplate extends ItemGuildWeaponTemplate{
	public List<Items> recycleRewards = Lists.newArrayList();
	
	public void init(){
		this.recycleRewards = ItemUtils.str2ItemList(this.getRecycle(), ",", ":");
	}

	public List<Items> getRecycleRewards() {
		return Lists.newArrayList(recycleRewards);
	}
	
	
}
