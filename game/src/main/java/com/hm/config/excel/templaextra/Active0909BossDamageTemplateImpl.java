package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0909BossDamageTemplate;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.Map;

@FileConfig("active_0909_boss_damage")
public class Active0909BossDamageTemplateImpl extends Active0909BossDamageTemplate {
    private WeightMeta<Integer> weightMeta;

    public void init() {
        Map<Integer, Integer> map = Maps.newConcurrentMap();
        for (String s : getCrit().split(",")) {
            int[] ints = StrUtil.splitToInt(s, ":");
            if (ints.length >= 2) {
                map.put(ints[0], ints[1]);
            }
        }
        if (!map.isEmpty()) {
            weightMeta = RandomUtils.buildWeightMeta(map);
        }

    }

    public int getRate() {
        if (weightMeta == null) {
            return 1;
        }

        return weightMeta.random();
    }
}