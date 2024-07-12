package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TaskBoxTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2018-11-21
 */
@FileConfig("task_box")
public class TaskBoxTemplateImpl extends TaskBoxTemplate {
    private List<Items> rewardList = Lists.newArrayList();
    public void init() {
        rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
