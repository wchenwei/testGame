package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.KfSeasonPassTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/4/14 20:21
 */
@FileConfig("kf_season_pass")
public class KfSeasonPassTemplateImpl extends KfSeasonPassTemplate {

    private List<Items> rewardFreeList;
    private List<Items> rewardTrumpList;
    private List<Items> rewardLegendList;

    public void init() {
        this.rewardFreeList = ItemUtils.str2DefaultItemImmutableList(this.getReward_free());
        this.rewardTrumpList = ItemUtils.str2DefaultItemImmutableList(this.getReward_trump());
        this.rewardLegendList = ItemUtils.str2DefaultItemImmutableList(this.getReward_legend());
    }

    public boolean isFit(int stage) {
        return stage == this.getStage();
    }

    public List<Items> getRewardFreeList() {
        return rewardFreeList;
    }

    public List<Items> getRewardTrumpList() {
        return rewardTrumpList;
    }

    public List<Items> getRewardLegendList() {
        return rewardLegendList;
    }
}
