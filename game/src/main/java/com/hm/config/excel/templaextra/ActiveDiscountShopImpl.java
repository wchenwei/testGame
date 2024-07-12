package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveDiscountShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_discount_shop")
public class ActiveDiscountShopImpl extends ActiveDiscountShopTemplate{
	private List<Items> rewardList = Lists.newArrayList();
	private List<Items> costList = Lists.newArrayList();
	private List<Items> resetList = Lists.newArrayList();
	private List<Items> resetAddList = Lists.newArrayList();
	
	public void init() {
        rewardList = ItemUtils.str2ItemList(this.getGoods(), ",", ":");
        costList = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
        resetList = ItemUtils.str2ItemList(this.getRefresh_price_base(), ",", ":");
        resetAddList = ItemUtils.str2ItemList(this.getRefresh_price_add(), ",", ":");
    }
    public List<Items> getRewardList() {
        return rewardList.stream().map(t ->t.clone()).collect(Collectors.toList());
    }
    public List<Items> getCostList() {
        return costList.stream().map(t ->t.clone()).collect(Collectors.toList());
    }
    
    public List<Items> getResetCostList() {
		return resetList.stream().map(t ->t.clone()).collect(Collectors.toList());
	}
	public List<Items> getResetAddCostList() {
		return resetAddList.stream().map(t ->t.clone()).collect(Collectors.toList());
	}
}
