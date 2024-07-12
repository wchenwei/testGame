package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveOnlineRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_online_reward")
public class ActiveOnlineRewardTemplateImpl extends ActiveOnlineRewardTemplate {
    private List<Items> rewardList = Lists.newArrayList();

    public void init() {
        rewardList = ItemUtils.str2DefaultItemImmutableList(getReward());
    }

    public List<Items> getRewardList() {
        return rewardList;
    }}
