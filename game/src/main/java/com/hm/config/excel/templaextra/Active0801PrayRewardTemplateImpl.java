package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0801PrayRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import com.hm.util.WeightMeta;

import java.util.List;
import java.util.Map;


@FileConfig("active_0801_pray_reward")
public class Active0801PrayRewardTemplateImpl extends Active0801PrayRewardTemplate {
    private List<Items> rewards = Lists.newArrayList();
    private WeightMeta<Integer> weightMeta;

    public void init() {
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
        // 1:6,2:14,3:30,4:30,5:14,6:6
        Map<Integer, Integer> map = StringUtil.strToMap(this.getRandom(), ",", ":");
        weightMeta = RandomUtils.buildWeightMeta(map);
    }

    public List<Items> getRewards() {
        return rewards;
    }

    public WeightMeta<Integer> getWeightMeta() {
        return weightMeta;
    }
}
