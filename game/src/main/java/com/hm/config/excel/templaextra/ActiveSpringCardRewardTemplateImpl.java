package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSpringCardRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_spring_card_reward")
public class ActiveSpringCardRewardTemplateImpl extends ActiveSpringCardRewardTemplate {
    private List<Items> rewardItems = Lists.newArrayList();

    public void init() {
        this.rewardItems = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardItems() {
        return rewardItems;
    }
}
