package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.Active0yuanGearTemplate;
import com.hm.config.excel.templaextra.Active0yuanGiftTemplateImpl;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description 0元礼包
 * @date 2021/2/24 10:15
 */
@Config
public class ZeroGiftConfig extends ExcleConfig {
    // id -- 数据
    private Map<Integer, Active0yuanGiftTemplateImpl> giftTemplateMap = Maps.newConcurrentMap();

    private int giftMaxDay;

    private Map<Integer, Active0yuanGearTemplate> gearTemplateMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
//        loadGiftTemplate();
//        loadGearTemplate();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active0yuanGearTemplate.class, Active0yuanGiftTemplateImpl.class);
    }

    public Active0yuanGearTemplate getGearTemplateById(int id) {
        Active0yuanGearTemplate template = gearTemplateMap.getOrDefault(id, null);
        return template;
    }

    public Active0yuanGiftTemplateImpl getTemplateImplById(int id) {
        Active0yuanGiftTemplateImpl template = giftTemplateMap.getOrDefault(id, null);
        return template;
    }

    public int getMaxDay() {
        return giftMaxDay;
    }

    private void loadGiftTemplate() {
        List<Active0yuanGiftTemplateImpl> list = JSONUtil.fromJson(getJson(Active0yuanGiftTemplateImpl.class), new TypeReference<List<Active0yuanGiftTemplateImpl>>() {
        });
        list.forEach(e -> {
            e.init();
            if (giftMaxDay < e.getDay()) {
                giftMaxDay = e.getDay();
            }
        });
        Map<Integer, Active0yuanGiftTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active0yuanGiftTemplateImpl::getId, Function.identity()));
        giftTemplateMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadGearTemplate() {
        List<Active0yuanGearTemplate> list = JSONUtil.fromJson(getJson(Active0yuanGearTemplate.class), new TypeReference<List<Active0yuanGearTemplate>>() {
        });
        Map<Integer, Active0yuanGearTemplate> tempMap = list.stream().collect(Collectors.toMap(Active0yuanGearTemplate::getId, Function.identity()));
        gearTemplateMap = ImmutableMap.copyOf(tempMap);
    }
}
