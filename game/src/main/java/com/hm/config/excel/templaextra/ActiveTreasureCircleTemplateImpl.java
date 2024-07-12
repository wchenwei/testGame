package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveTreasureCircleTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-06-21
 */
@FileConfig("active_treasure_circle")
public class ActiveTreasureCircleTemplateImpl extends ActiveTreasureCircleTemplate {
    private List<Items> rewardList = Lists.newArrayList();

    public void init() {
        this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
