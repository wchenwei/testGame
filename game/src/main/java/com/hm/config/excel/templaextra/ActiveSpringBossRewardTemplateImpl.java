package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSpringBossRewardTemplate;
import com.hm.model.item.Items;
import com.hm.model.item.WeightItem;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FileConfig("active_spring_boss_reward")
public class ActiveSpringBossRewardTemplateImpl extends ActiveSpringBossRewardTemplate {
    private List<WeightItem> rewardItemsList = Lists.newArrayList();
    private List<Items> itemList = Lists.newArrayList();
    private WeightMeta<Items> weightMeta;

    public void init() {
        for (String itemStr : getReward_random().split(",")) {
            WeightItem weightItem = ItemUtils.str2WeightItem(itemStr, ":");
            if (weightItem != null) {
                rewardItemsList.add(weightItem);
            }
        }

        if (CollUtil.isNotEmpty(rewardItemsList)) {
            Map<Items, Integer> map = rewardItemsList.stream().collect(Collectors.toMap(WeightItem::getItems, WeightItem::getWeight));
            weightMeta = RandomUtils.buildWeightMeta(map);
        }

        this.itemList = ItemUtils.str2ItemList(getReward_final(), ",", ":");
    }

    public Items pickRandomReward() {
        if (weightMeta != null && CollUtil.isNotEmpty(rewardItemsList)) {
            return weightMeta.random();
        }
        return null;
    }

    public List<Items> getFinalReward() {
        return itemList;
    }
}
