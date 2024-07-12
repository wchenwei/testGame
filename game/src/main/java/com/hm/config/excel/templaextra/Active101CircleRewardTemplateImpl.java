package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active101CircleRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-08-09
 */
@FileConfig("active_101_circle_reward")
public class Active101CircleRewardTemplateImpl extends Active101CircleRewardTemplate {
    private List<Items> rewards = Lists.newArrayList();

    public void init() {
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
    }

    public List<Items> getRewards() {
        return rewards;
    }
}
