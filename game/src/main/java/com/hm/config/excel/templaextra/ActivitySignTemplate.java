package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSignTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_sign")
public class ActivitySignTemplate extends ActiveSignTemplate{
	private List<Items> itemList = Lists.newArrayList();
	
	public void init() {
		this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":");
	}
	
	public List<Items> getItemList(BasePlayer player) {
		int vipDouble = getVip_double();
		List<Items> tempList = Lists.newArrayList();
		for (Items items : itemList) {
			Items clones = items.clone();
			if(vipDouble >= 1 && player.getPlayerVipInfo().getVipLv() >= vipDouble) {
				clones.setCount(clones.getCount()*2);
			}
			tempList.add(clones);
		}
		return tempList;
	}
}
