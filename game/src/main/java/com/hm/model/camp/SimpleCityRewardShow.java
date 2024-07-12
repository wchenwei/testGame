package com.hm.model.camp;

import com.google.common.collect.Lists;
import com.hm.model.item.Items;
import lombok.Data;

import java.util.List;

@Data
public class SimpleCityRewardShow {
    private List<Items> rewards = Lists.newArrayList();
    private int[] cityNum;//每个类型城市的数量
    private List<Integer> areas = Lists.newArrayList();

}
