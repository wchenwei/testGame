package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveTreasureCircleTemplateImpl;
import com.hm.config.excel.templaextra.ActiveTreasureShaiTemplateImpl;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-06-21
 */
@Config
public class TreasureMapConfig extends ExcleConfig {
    /**
     * id:obj
     */
    private Map<Integer, ActiveTreasureCircleTemplateImpl> circleMap = Maps.newConcurrentMap();
    private WeightMeta<Integer> weightMeta;
    /**
     * id:obj
     */
    private Map<Integer, ActiveTreasureShaiTemplateImpl> shaiMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
//        List<ActiveTreasureCircleTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveTreasureCircleTemplateImpl.class), new TypeReference<List<ActiveTreasureCircleTemplateImpl>>() {
//        });
//        Map<Integer, Integer> wm = Maps.newConcurrentMap();
//        Map<Integer, ActiveTreasureCircleTemplateImpl> tmpCircleMap = Maps.newConcurrentMap();
//        list.forEach(t -> {
//            t.init();
//            wm.put(t.getId(), t.getRate());
//            tmpCircleMap.put(t.getId(), t);
//        });
//
//        weightMeta = RandomUtils.buildWeightMeta(wm);
//        circleMap = ImmutableMap.copyOf(tmpCircleMap);
//
//        List<ActiveTreasureShaiTemplateImpl> list1 = JSONUtil.fromJson(getJson(ActiveTreasureShaiTemplateImpl.class), new TypeReference<List<ActiveTreasureShaiTemplateImpl>>() {
//        });
//        Map<Integer, ActiveTreasureShaiTemplateImpl> tmpShaiMap = Maps.newConcurrentMap();
//        list1.forEach(t -> {
//            t.init();
//            tmpShaiMap.put(t.getId(), t);
//        });
//
//        shaiMap = ImmutableMap.copyOf(tmpShaiMap);
    }

    public ActiveTreasureCircleTemplateImpl getCircleCfg(int id) {
        return circleMap.getOrDefault(id, null);
    }

    public ActiveTreasureShaiTemplateImpl getShaiCfg(int id) {
        return shaiMap.getOrDefault(id, null);
    }

    public int pickCircleId() {
        return weightMeta.random();
    }

    public int pickShaiId() {
        return RandomUtils.randomEle(new ArrayList<>(shaiMap.keySet()));
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveTreasureCircleTemplateImpl.class, ActiveTreasureShaiTemplateImpl.class);
    }
}
