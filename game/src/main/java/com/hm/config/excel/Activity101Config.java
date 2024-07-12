package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active101CircleRewardTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2019-08-09
 */
@Config
public class Activity101Config extends ExcleConfig {
    /**
     * Active101CircleRewardTemplateImpl::getId : Active101CircleRewardTemplateImpl
     */
    private Map<Integer, Active101CircleRewardTemplateImpl> circleMap = Maps.newConcurrentMap();

    /**
     * Active101PrayRewardTemplateImpl::getId : Active101PrayRewardTemplateImpl
     */
    private Map<Integer, Active101PrayRewardTemplateImpl> prayRewardMap = Maps.newConcurrentMap();

    /**
     * Active101ShopTemplateImpl::getId : Active101ShopTemplateImpl
     */
    private Map<Integer, Active101ShopTemplateImpl> shopMap = Maps.newConcurrentMap();

    private List<Active101TreasureRateFirstTemplateImpl> rateFirstList = Lists.newArrayList();
    private List<Active101TreasureRateTemplateImpl> rateList = Lists.newArrayList();

    // ===================================================================

    /**
     * 随机一次获得转盘奖励
     *
     * @param record 已领取次数记录
     * @return
     */
    public Active101CircleRewardTemplateImpl randomCircleRewards(Map<Integer, Integer> record) {
        // 已领取次数 + 1
        int times = 1 + record.values().stream().mapToInt(value -> value).sum();

        // Active101CircleRewardTemplate::getId : Active101CircleRewardTemplate::getRate
        Map<Integer, Integer> map = circleMap.values().stream().filter(e -> e.getNumber() <= times && record.getOrDefault(e.getId(), 0) < e.getTimes()).
                collect(Collectors.toMap(Active101CircleRewardTemplate::getId, Active101CircleRewardTemplate::getRate, (a, b) -> b));

        if (map.isEmpty()) {
            return null;
        }

        Integer randomId = RandomUtils.buildWeightMeta(map).random();

        return circleMap.getOrDefault(randomId, null);
    }

    /**
     * 随机祈福奖励
     *
     * @param id Active101PrayRewardTemplateImpl::getId
     * @return
     */
    public Active101PrayRewardTemplateImpl calcPrayRewards(int id) {
        return prayRewardMap.getOrDefault(id, null);
    }

    public Active101ShopTemplateImpl getShopCfg(int id) {
        return shopMap.getOrDefault(id, null);
    }

    /**
     * 获取赠送比例
     *
     * @param value
     * @return
     */
    public double getTreasureRate(int value) {
        Active101TreasureRateTemplateImpl cfg = rateList.stream().
                filter(t -> t.getRecharge_down() <= value && value <= t.getRecharge_up()).
                findFirst().orElse(null);
        if (cfg == null) {
            return 0;
        }
        double v;
        // 上下限一样的
        if (cfg.getRate_down().equals(cfg.getRate_up())) {
            v = cfg.getRate_down();
        } else {
            v = RandomUtils.randomDouble(cfg.getRate_down(), cfg.getRate_up());
        }
        return v * cfg.getWeightMeta().random();
    }

    /**
     * 获取赠送比例(首轮)
     *
     * @param value
     * @return
     */
    public double getTreasureRateFirst(int value) {
        Active101TreasureRateFirstTemplateImpl cfg = rateFirstList.stream().
                filter(t -> t.getRecharge_down() <= value && value <= t.getRecharge_up()).
                findFirst().orElse(null);
        if (cfg == null) {
            return 0;
        }
        double v;
        if (cfg.getRate_down().equals(cfg.getRate_up())) {
            v = cfg.getRate_down();
        } else {
            v = RandomUtils.randomDouble(cfg.getRate_down(), cfg.getRate_up());
        }
        return v * cfg.getWeightMeta().random();
    }
    // ===================================================================

    @Override
    public void loadConfig() {
        /*Map<Integer, Active101CircleRewardTemplateImpl> tmpCircleMap = Maps.newConcurrentMap();
        List<Active101CircleRewardTemplateImpl> list0 = JSONUtil.fromJson(getJson(Active101CircleRewardTemplateImpl.class), new TypeReference<List<Active101CircleRewardTemplateImpl>>() {
        });
        list0.forEach(e -> {
            e.init();
            tmpCircleMap.put(e.getId(), e);
        });
        circleMap = ImmutableMap.copyOf(tmpCircleMap);
        // =====================================================================
        Map<Integer, Active101PrayRewardTemplateImpl> tmpPrayRewardMap = Maps.newConcurrentMap();
        List<Active101PrayRewardTemplateImpl> list1 = JSONUtil.fromJson(getJson(Active101PrayRewardTemplateImpl.class), new TypeReference<List<Active101PrayRewardTemplateImpl>>() {
        });
        list1.forEach(e -> {
            e.init();
            tmpPrayRewardMap.put(e.getId(), e);
        });
        prayRewardMap = ImmutableMap.copyOf(tmpPrayRewardMap);
        // =====================================================================
        Map<Integer, Active101ShopTemplateImpl> tmpShopMap = Maps.newConcurrentMap();
        List<Active101ShopTemplateImpl> list3 = JSONUtil.fromJson(getJson(Active101ShopTemplateImpl.class), new TypeReference<List<Active101ShopTemplateImpl>>() {
        });
        list3.forEach(e -> {
            e.init();
            tmpShopMap.put(e.getId(), e);
        });
        shopMap = ImmutableMap.copyOf(tmpShopMap);
        // =====================================================================
        List<Active101TreasureRateFirstTemplateImpl> list4 = JSONUtil.fromJson(getJson(Active101TreasureRateFirstTemplateImpl.class), new TypeReference<List<Active101TreasureRateFirstTemplateImpl>>() {
        });

        list4.forEach(Active101TreasureRateFirstTemplateImpl::init);
        rateFirstList = ImmutableList.copyOf(list4);
        // =====================================================================
        List<Active101TreasureRateTemplateImpl> list5 = JSONUtil.fromJson(getJson(Active101TreasureRateTemplateImpl.class), new TypeReference<List<Active101TreasureRateTemplateImpl>>() {
        });

        list5.forEach(Active101TreasureRateTemplateImpl::init);
        rateList = ImmutableList.copyOf(list5);*/
        // =====================================================================
    }


    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active101CircleRewardTemplateImpl.class,
                Active101PrayRewardTemplateImpl.class,
                Active101ShopTemplateImpl.class,
                Active101TreasureRateFirstTemplateImpl.class,
                Active101TreasureRateTemplateImpl.class
        );
    }
}
