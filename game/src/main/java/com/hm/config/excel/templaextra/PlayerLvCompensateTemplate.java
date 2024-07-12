package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveExpGiftTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_exp_gift")
public class PlayerLvCompensateTemplate extends ActiveExpGiftTemplate{
	private List<Items> itemList = Lists.newArrayList();
	
	public void init(){
		this.itemList = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}

	public List<Items> getItemList() {
		return itemList;
	}
	
	public boolean isFit(int lv) {
		return lv >= getPlayer_lv_down() && lv <= getPlayer_lv_up();
	}
}





