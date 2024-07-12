package com.hmkf.config.extra;

import java.util.List;

import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hmkf.config.template.KfPkRewardVisitTemplate;

@FileConfig("kf_pk_reward_visit")
public class KfLevelRewardVisitTemplate extends KfPkRewardVisitTemplate {
    private List<Items> rewardList;

    public void init() {
        this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }

    public boolean isFit(int rank) {
        return rank >= getSever_lv_down() && rank <= getSever_lv_up();
    }
}
