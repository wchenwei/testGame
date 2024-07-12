package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveHolidayGiftTemplate;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 假日庆典
 * @author chenwei
 * @date 2020年2月11日 下午1:30:57
 * @version V1.0
 */
@Config
public class ActivityHolidayConfig extends ExcleConfig {
    private Map<Integer, ActiveHolidayGiftTemplate> holidayGiftMap = Maps.newConcurrentMap();
    @Override
    public void loadConfig() {
       /* Map<Integer, ActiveHolidayGiftTemplate> tempGift = Maps.newHashMap();
        List<ActiveHolidayGiftTemplate> tempList = JSONUtil.fromJson(getJson(ActiveHolidayGiftTemplate.class), new TypeReference<List<ActiveHolidayGiftTemplate>>() {
        });
        tempList.forEach(e -> {
            tempGift.put(e.getRecharge_id(), e);
        });
        holidayGiftMap = ImmutableMap.copyOf(tempGift);*/
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveHolidayGiftTemplate.class);
    }

    public ActiveHolidayGiftTemplate getHolidayGiftTemplate(int rechargeId){
        return holidayGiftMap.get(rechargeId);
    }
}
