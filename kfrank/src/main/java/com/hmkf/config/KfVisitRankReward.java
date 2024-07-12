package com.hmkf.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.hmkf.config.extra.KfLevelRewardVisitTemplate;

public class KfVisitRankReward {
    private List<KfLevelRewardVisitTemplate> rankList = Lists.newArrayList();

    public KfVisitRankReward() {
    }

    public void addRankTemplate(KfLevelRewardVisitTemplate e) {
        this.rankList.add(e);
    }


    public KfLevelRewardVisitTemplate getRankTemplate(int rank) {
        return this.rankList.stream().filter(e -> e.isFit(rank)).findAny().orElse(null);
    }

}
