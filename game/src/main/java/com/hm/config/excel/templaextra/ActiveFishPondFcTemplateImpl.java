package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.ActiveFishPondFcTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import com.hm.util.WeightMeta;

import java.util.List;
import java.util.Map;

/**
 * @introduce: 类注释
 * @author: wyp
 * @DATE: 2023/11/1
 **/
@FileConfig("active_fish_pond_fc")
public class ActiveFishPondFcTemplateImpl extends ActiveFishPondFcTemplate {

    private List<Integer> fishList;
    // 鱼饵道具Id -- 鱼权重
    private Map<Integer, WeightMeta<Integer>> weightMetaMap = Maps.newHashMap();

    public void init() {
        fishList = StringUtil.splitStr2IntegerList(this.getType(), ",");
        for (String str : StringUtil.splitStr2StrList(this.getBait(), "#")) {
            String[] s = str.split("_");
            Map<Integer, Integer> map = StringUtil.strToMap(s[1], ",", ":");
            // 移除不在本 渔场的鱼权重数据
            map.keySet().removeIf(fishId-> !CollUtil.contains(fishList, fishId));
            if(CollUtil.isEmpty(map)){
                continue;
            }
            weightMetaMap.put(Convert.toInt(s[0]), RandomUtils.buildWeightMeta(map));
        }
    }

    public boolean containsItem(int itemId){
        return weightMetaMap.containsKey(itemId);
    }


}
