package com.hmkf.config.extra;

import java.util.List;

import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hmkf.config.template.KfPkRewardRankTemplate;
import lombok.Getter;

@Getter
@FileConfig("kf_pk_reward_rank")
public class KfLevelRewardRankTemplate extends KfPkRewardRankTemplate {
    private List<Items> rewardList;
    private List<Items> weekRewardList;

    public void init() {
        this.rewardList = ItemUtils.str2DefaultItemList(getReward());
        this.weekRewardList = ItemUtils.str2DefaultItemList(getWeekly_reward());
    }

    public boolean isFit(int rank) {
        return rank >= getRank_down() && rank <= getRank_up();
    }
}
