package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ServiceMergeSignTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("service_merge_sign")
public class ServiceMergeSignTemplateImpl extends ServiceMergeSignTemplate {
    private List<Items> rewardItems = Lists.newArrayList();
    private List<Items> rewardRechargeItems = Lists.newArrayList();

    public void init() {
        rewardItems = ItemUtils.str2ItemList(this.getReward_sign(), ",", ":");
        rewardRechargeItems = ItemUtils.str2ItemList(this.getReward_recharge(), ",", ":");
    }

    public List<Items> getRewardItems() {
        return rewardItems;
    }

    public List<Items> getRewardRechargeItems() {
        return rewardRechargeItems;
    }

    public boolean inRange(int n) {
        return getLv_down() <=n && n <= getLv_up();
    }
}
