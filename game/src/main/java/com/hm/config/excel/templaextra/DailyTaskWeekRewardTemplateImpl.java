package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.DailyTaskWeekRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-11-12
 */
@FileConfig("daily_task_week_reward")
public class DailyTaskWeekRewardTemplateImpl extends DailyTaskWeekRewardTemplate {
    private List<Items> baseItemsList = Lists.newArrayList();
    private List<Items> highItemsList = Lists.newArrayList();

    public void init() {
        baseItemsList = ItemUtils.str2ItemList(getReward_base(), ",", ":");
        highItemsList = ItemUtils.str2ItemList(getReward_high(), ",", ":");
    }

    public List<Items> getBaseItemsList() {
        return baseItemsList;
    }

    public List<Items> getHighItemsList() {
        return highItemsList;
    }
}
