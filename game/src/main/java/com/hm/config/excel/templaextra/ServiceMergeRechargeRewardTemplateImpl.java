package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ServiceMergeRechargeRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-29
 */
@FileConfig("service_merge_recharge_reward")
public class ServiceMergeRechargeRewardTemplateImpl extends ServiceMergeRechargeRewardTemplate {
    private List<Items> rewardItems = Lists.newArrayList();

    public void init() {
        rewardItems = ItemUtils.str2ItemList(this.getReward(), ",", ":");
    }

    public List<Items> getRewardItems() {
        return rewardItems;
    }
}
