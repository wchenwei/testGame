package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active51TreasureRateTemplate;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import com.hm.util.WeightMeta;

import java.util.Map;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-04-07 15:37
 **/
@FileConfig("active_51_treasure_rate")
public class Active51TreasureRateTemplateImpl extends Active51TreasureRateTemplate {
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
