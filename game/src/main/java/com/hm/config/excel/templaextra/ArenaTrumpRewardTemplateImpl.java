package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ArenaTrumpRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-04
 *
 * @author Administrator
 */
@FileConfig("arena_trump_reward")
public class ArenaTrumpRewardTemplateImpl extends ArenaTrumpRewardTemplate {
    private List<Items> rewardItem = Lists.newArrayList();

    public void init() {
        rewardItem = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardItem() {
        return rewardItem;
    }

    public void setRewardItem(List<Items> rewardItem) {
        this.rewardItem = rewardItem;
    }
}
