package com.hmkf.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.hmkf.config.extra.KfLevelRewardRankTemplate;

public class KfRankReward {
    private List<KfLevelRewardRankTemplate> rankList = Lists.newArrayList();

    public KfRankReward() {
    }

    public void addRankTemplate(KfLevelRewardRankTemplate e) {
        this.rankList.add(e);
    }


    public KfLevelRewardRankTemplate getRankTemplate(int rank) {
        return this.rankList.stream().filter(e -> e.isFit(rank)).findAny().orElse(null);
    }
}
