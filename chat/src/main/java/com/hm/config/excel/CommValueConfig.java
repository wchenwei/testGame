package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.GameConstants;
import com.hm.config.template.CommonValueTemplate;
import com.hm.enums.CommonValueType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Config
public class CommValueConfig extends ExcleConfig {

    private Map<Integer, CommonValueTemplate> configMap = Maps.newHashMap();

    private int[] blackHouseDynamicLv;
    private int[] blackHouseDynamicVipLv;

    private int[] blackHouseTime;
    private int[] maxCounts;

    public int getIntValue(CommonValueType type) {
        return configMap.get(type.getId()).getValue().intValue();
    }

    public String getStrValue(CommonValueType type) {
        return configMap.get(type.getId()).getValue_2();
    }

    @Override
    public void loadConfig() {
        configMap = JSONUtil.fromJson(getJson(CommonValueTemplate.class),
                new TypeReference<ArrayList<CommonValueTemplate>>() {
                }).stream()
                .collect(Collectors.toMap(CommonValueTemplate::getId, Function.identity()));

        initBlackHouse();
        log.info(String.format("常量配置加载完成，size：%s", configMap.size()));
    }

    private void initBlackHouse() {
//        blackHouseDynamicLv = toIntegerArray(CommonValueType.BLACK_HOUSE_DYNAMIC_LV);
//        blackHouseTime = toIntegerArray(CommonValueType.BLACK_HOUSE_SECONDS);
//        blackHouseDynamicVipLv = toIntegerArray(CommonValueType.BLACK_HOUSE_DYNAMIC_VIP_LV);
//        maxCounts = toIntegerArray(CommonValueType.BLACK_HOUSE_Chat_Size);
    }

    private int[] toIntegerArray(CommonValueType config) {
        return StringUtil.splitStr2IntArray(getStrValue(config),GameConstants.SPLIT1);
    }


    /**
     * 根据禁言次数获取禁言时长，3次以后都取3次
     */
    public int getBlackHouseTime(int count) {
        int index = count - 1;
        if (index >= blackHouseTime.length) {
            return blackHouseTime[blackHouseTime.length - 1];
        }
        return blackHouseTime[index];
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(CommonValueTemplate.class);
    }
}
