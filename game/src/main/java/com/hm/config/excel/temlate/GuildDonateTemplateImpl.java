package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.enums.ItemType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("guild_donate")
public class GuildDonateTemplateImpl extends GuildDonateTemplate{
	private List<Items> itemsCost = Lists.newArrayList();
	private List<Items> itemsReward = Lists.newArrayList();

	@ConfigInit
	public void init() {
		itemsCost = ItemUtils.str2ItemList(this.getCost(), ",", ":");
		itemsReward = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	
	public List<Items> getItemsCost() {
		return itemsCost;
	}
	public List<Items> getItemsReward() {
		return itemsReward;
	}
	//获取贡献奖励数据
	public long getContr() {
		for(Items item: itemsReward) {
			if(item.getEnumItemType()==ItemType.CURRENCY && item.getId()==PlayerAssetEnum.Credit.getTypeId()) {
				return item.getCount();
			}
		}
		return 0l;
	}
}
