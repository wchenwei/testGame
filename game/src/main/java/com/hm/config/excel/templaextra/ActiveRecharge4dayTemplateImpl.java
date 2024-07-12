package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRecharge4dayTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-10
 */
@FileConfig("active_recharge_4day")
public class ActiveRecharge4dayTemplateImpl extends ActiveRecharge4dayTemplate {
    private List<Items> rewardList = Lists.newArrayList();

    public void init() {
        this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
