package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSscarRewardLibraryTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

@FileConfig("active_SScar_reward_library")
public class ActiveSscarRewardLibraryTemplateImpl extends ActiveSscarRewardLibraryTemplate {
    private Items rewardItems;

    public void init() {
        rewardItems = ItemUtils.str2Item(getReward(), ":");
    }

    public Items getRewardItems() {
        return rewardItems;
    }
}
