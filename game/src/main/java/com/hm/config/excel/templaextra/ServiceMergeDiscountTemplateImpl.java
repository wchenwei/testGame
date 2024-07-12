package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ServiceMergeDiscountTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-29
 */
@FileConfig("service_merge_discount")
public class ServiceMergeDiscountTemplateImpl extends ServiceMergeDiscountTemplate {
    private Items rewardItems;
    public void init() {
        rewardItems = ItemUtils.str2Item(getReward(), ":");
    }

    public Items getRewardItems() {
        return rewardItems;
    }
}
