package com.hmkf.config.extra;

import java.util.List;

import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hmkf.config.template.KfPkRewardSeasonTemplate;

@FileConfig("kf_pk_reward_season")
public class KfLevelRewardSeasonTemplate extends KfPkRewardSeasonTemplate {
    private List<Items> rewardList;

    public void init() {
        this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
