package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveTimeLimitedShopTemplateImpl;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-08-22
 */
@Config
public class ActivityLimitDiscountConfig extends ExcleConfig {
    /**
     * ActiveTimeLimitedShopTemplateImpl::getId : ActiveTimeLimitedShopTemplateImpl
     */
    private Map<Integer, ActiveTimeLimitedShopTemplateImpl> shopMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
//        List<ActiveTimeLimitedShopTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveTimeLimitedShopTemplateImpl.class), new TypeReference<List<ActiveTimeLimitedShopTemplateImpl>>() {
//        });
//
//        Map<Integer, ActiveTimeLimitedShopTemplateImpl> m = Maps.newConcurrentMap();
//        list.forEach(e -> {
//            e.init();
//            m.put(e.getId(), e);
//        });
//
//        shopMap = ImmutableMap.copyOf(m);
    }

    public ActiveTimeLimitedShopTemplateImpl getCfg(int id) {
        return shopMap.getOrDefault(id, null);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveTimeLimitedShopTemplateImpl.class);
    }
}
