package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active41PassTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/3/21 10:02
 */
@FileConfig("active_41_pass")
public class Active41PassTemplateImpl extends Active41PassTemplate {
    private List<Items> rewardFreeList;
    private List<Items> rewardTrumpList;
    private List<Items> rewardLegendList;

    public void init() {
        this.rewardFreeList = ItemUtils.str2DefaultItemImmutableList(this.getReward_free());
        this.rewardTrumpList = ItemUtils.str2DefaultItemImmutableList(this.getReward_trump());
        this.rewardLegendList = ItemUtils.str2DefaultItemImmutableList(this.getReward_legend());
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

    public boolean isFit(int version, int playerLv) {
        return version == this.getStage() && playerLv >= this.getPlayer_lv_down() && playerLv <= this.getPlayer_lv_up();
    }
}
