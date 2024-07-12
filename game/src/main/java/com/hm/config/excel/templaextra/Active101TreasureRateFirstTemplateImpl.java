package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active101TreasureRateFirstTemplate;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import com.hm.util.WeightMeta;

import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-08-09
 */
@FileConfig("active_101_treasure_rate_first")
public class Active101TreasureRateFirstTemplateImpl extends Active101TreasureRateFirstTemplate {
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
