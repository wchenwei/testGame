package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import com.hm.util.WeightMeta;

import java.util.Map;

@FileConfig("factory")
public class FactoryTemplateImpl extends FactoryTemplate {
    private WeightMeta<Integer> weightMeta;

    public void init() {
        // 1:6,2:14,3:30,4:30,5:14,6:6
        Map<Integer, Integer> map = StringUtil.strToMap(this.getNumbers(), ",", ":");
        weightMeta = RandomUtils.buildWeightMeta(map);
    }


    public int randomNum() {
        return weightMeta.random();
    }
}
