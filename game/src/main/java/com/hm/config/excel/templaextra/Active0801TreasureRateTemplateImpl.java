package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0801TreasureRateTemplate;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import com.hm.util.WeightMeta;

import java.util.Map;

@FileConfig("active_0801_treasure_rate")
public class Active0801TreasureRateTemplateImpl extends Active0801TreasureRateTemplate {
    private WeightMeta<Double> weightMeta;

    public void init() {
        // 1:20,2:20,3:20,4:20,5:20
        Map<Double, Integer> map = StringUtil.strToDoubleMap(this.getLucky(), ",", ":");
        weightMeta = RandomUtils.buildWeightMeta(map);
    }

    public WeightMeta<Double> getWeightMeta() {
        return weightMeta;
    }
}
