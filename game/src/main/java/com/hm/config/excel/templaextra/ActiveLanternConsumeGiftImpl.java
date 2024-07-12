package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveLanternConsumeGiftTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_lantern_consume_gift")
public class ActiveLanternConsumeGiftImpl extends ActiveLanternConsumeGiftTemplate {
	private List<Items> rewards = Lists.newArrayList();

    public void init() {
        this.rewards = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewards() {
		return rewards.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	public boolean checkLv(int lv) {
		return lv>=this.getServer_level_low() && lv<= this.getServer_level_high();
	}
}
