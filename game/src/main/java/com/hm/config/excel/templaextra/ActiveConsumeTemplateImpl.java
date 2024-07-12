package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveConsumeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-02-25
 */
@FileConfig("active_consume")
public class ActiveConsumeTemplateImpl extends ActiveConsumeTemplate {
    private List<Items> rewardList = Lists.newArrayList();

    public void init() {
        rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}

