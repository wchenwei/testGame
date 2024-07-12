package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActivePrivateGiftItemTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_private_gift_item")
public class ActivePrivateGiftItemImpl extends ActivePrivateGiftItemTemplate {
	private List<Items> rewards = Lists.newArrayList();

    public void init() {
        this.rewards = ItemUtils.str2ItemList(this.getGoods(), ",", ":");
    }

    public List<Items> getRewardItems() {
        return rewards.stream().map(t -> t.clone()).collect(Collectors.toList());
    }
}
