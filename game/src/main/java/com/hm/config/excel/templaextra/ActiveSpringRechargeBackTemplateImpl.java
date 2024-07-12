package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSpringRechargeBackTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_spring_recharge_back")
public class ActiveSpringRechargeBackTemplateImpl extends ActiveSpringRechargeBackTemplate {
    private List<Items> backItems = Lists.newArrayList();
    private List<Items> rewardItems = Lists.newArrayList();

    public void init() {
        this.backItems = ItemUtils.str2ItemList(getFeed_back(), ",", ":");
        this.rewardItems = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getBackItems() {
        return backItems;
    }

    public List<Items> getRewardItems() {
        return rewardItems;
    }
}
