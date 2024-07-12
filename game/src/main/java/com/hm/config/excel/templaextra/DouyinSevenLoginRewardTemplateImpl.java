package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.DouyinSevenLoginRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("douyin_seven_login_reward")
public class DouyinSevenLoginRewardTemplateImpl extends DouyinSevenLoginRewardTemplate {
    private List<Items> rewardList = Lists.newArrayList();

    public void init() {
        rewardList = ItemUtils.str2DefaultItemImmutableList(getReward());
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
