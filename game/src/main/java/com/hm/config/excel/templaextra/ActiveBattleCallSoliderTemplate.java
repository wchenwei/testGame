package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.ActiveTankOrderConfigTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_tank_order_config")
public class ActiveBattleCallSoliderTemplate extends ActiveTankOrderConfigTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Integer> prices = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.prices = StringUtil.splitStr2IntegerList(this.getDouble_price(), ",");
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public int getPrice(int times){
		return this.prices.get(times-1);
	}
	

}
