package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.KfSeasonShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("kf_season_shop")
public class KfSeasonShopTemplateImpl extends KfSeasonShopTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Items> prices = Lists.newArrayList();
	public void init() {
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.prices = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public List<Items> getPrices() {
		return prices;
	}
	
	public boolean isFit(int serverLv,int type) {
		return serverLv>=this.getServer_lv_down()&&serverLv<=this.getServer_lv_up()&&this.getType()==type;
	}
}
