package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRecharge4dayNewTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_recharge_4day_new")
public class ActiveRecharge4dayNewTemplateImpl extends ActiveRecharge4dayNewTemplate {
    private List<Items> rewardList = Lists.newArrayList();

    public void init() {
        this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
