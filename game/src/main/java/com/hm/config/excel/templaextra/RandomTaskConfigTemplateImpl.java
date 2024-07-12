package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.RandomTaskConfigTemplate;
import com.hm.model.item.Items;
import com.hm.model.item.WeightItem;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import com.hm.util.WeightMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:地图民情事件
 * User: yang xb
 * Date: 2019-04-11
 *
 * @author Administrator
 */
@FileConfig("random_task_config")
public class RandomTaskConfigTemplateImpl extends RandomTaskConfigTemplate {
    private List<WeightItem> targetItemsList = Lists.newArrayList();
    private WeightMeta<Items> weightMeta;


    public void init() {
        if (!StringUtil.isNullOrEmpty(getTarget())) {
            for (String itemStr : getTarget().split(",")) {
                WeightItem weightItem = ItemUtils.str2WeightItem(itemStr, ":");
                if (weightItem != null) {
                    targetItemsList.add(weightItem);
                }
            }
        }

        if (CollUtil.isNotEmpty(targetItemsList)) {
            Map<Items, Integer> map = targetItemsList.stream().collect(Collectors.toMap(WeightItem::getItems, WeightItem::getWeight, (k, v) -> v));
            weightMeta = RandomUtils.buildWeightMeta(map);
        }
    }

    /**
     * 随机获取完成条件需要的道具!如果有的话
     *
     * @return
     */
    public Items pickARandomTargetItems() {
        if (weightMeta != null && CollUtil.isNotEmpty(targetItemsList)) {
            return weightMeta.random();
        }
        return null;
    }
}
