package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0yuanGiftTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/2/24 10:12
 */
@FileConfig("active_0yuan_gift")
public class Active0yuanGiftTemplateImpl extends Active0yuanGiftTemplate {

    private List<Items> rewardList = Lists.newArrayList();

    public void init() {
        this.rewardList = ItemUtils.str2DefaultItemImmutableList(getReward_back());
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
