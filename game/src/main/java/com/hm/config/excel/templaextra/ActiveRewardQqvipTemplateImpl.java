package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRewardQqvipTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/8/4 17:57
 */
@FileConfig("active_reward_qqvip")
public class ActiveRewardQqvipTemplateImpl extends ActiveRewardQqvipTemplate {
    private List<Items> reward = Lists.newArrayList();

    public void init() {
        this.reward = ItemUtils.str2DefaultItemImmutableList(this.getReward());
    }

    public List<Items> getRewardItems() {
        return reward;
    }
}
