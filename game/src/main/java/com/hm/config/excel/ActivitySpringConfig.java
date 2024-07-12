package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveSpringBossTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Config
public class ActivitySpringConfig extends ExcleConfig {
    /**
     * ActiveSpringBossTemplate::getId : ActiveSpringBossTemplate
     */
    private Map<Integer, ActiveSpringBossTemplate> bossTemplateMap = Maps.newConcurrentMap();
    /**
     * ActiveSpringBossRewardTemplateImpl::getId : ActiveSpringBossRewardTemplateImpl
     */
    private Map<Integer, ActiveSpringBossRewardTemplateImpl> bossRewardTemplateMap = Maps.newConcurrentMap();
    /**
     * ActiveSpringCardTemplateImpl::getId : ActiveSpringCardTemplateImpl
     */
    private Map<Integer, ActiveSpringCardTemplateImpl> cardTemplateMap = Maps.newConcurrentMap();
    /**
     * ActiveSpringCardRewardTemplateImpl::getId : ActiveSpringCardRewardTemplateImpl
     */
    private Map<Integer, ActiveSpringCardRewardTemplateImpl> cardRewardTemplateMap = Maps.newConcurrentMap();
    /**
     * ActiveSpringRechargeBackTemplateImpl::getId : ActiveSpringRechargeBackTemplateImpl
     */
    private Map<Integer, ActiveSpringRechargeBackTemplateImpl> rechargeBackTemplateMap = Maps.newConcurrentMap();
    private List<ActiveSpringRateFirstTemplateImpl> rateFirstTemplateList = Lists.newArrayList();
    private List<ActiveSpringTreasureRateTemplateImpl> treasureRateTemplateList = Lists.newArrayList();
    private List<ActiveSpringBossDamageTemplateImpl> bossDamageTemplateList = Lists.newArrayList();

    public ActiveSpringCardTemplateImpl getCardCfg(int id) {
        return cardTemplateMap.getOrDefault(id, null);
    }

    public ActiveSpringCardRewardTemplateImpl getCardRewardCfg(int id) {
        return cardRewardTemplateMap.getOrDefault(id, null);
    }

    /**
     * 根据vip point 获取 配置信息
     *
     * @param point
     * @return
     */
    public ActiveSpringRechargeBackTemplateImpl queryByVipPoint(int point) {
        Optional<ActiveSpringRechargeBackTemplateImpl> first = rechargeBackTemplateMap.values().stream()
                .filter(e -> e.getCharge_gold().equals(point)).findFirst();
        return first.orElse(null);
    }

    public ActiveSpringRechargeBackTemplateImpl getRechargeBackCfg(int id) {
        return rechargeBackTemplateMap.getOrDefault(id, null);
    }


    public ActiveSpringBossRewardTemplateImpl getBossRewardCfg(int bossLv, int playerLv) {
        ActiveSpringBossRewardTemplateImpl cfg = bossRewardTemplateMap.values().stream().filter(e -> e.getBoss_lv().equals(bossLv))
                .filter(e -> e.getPlayer_lv_down() <= playerLv && playerLv <= e.getPlayer_lv_up()).findFirst().orElse(null);
        return cfg;
    }

    public ActiveSpringBossTemplate getBossCfg(int id) {
        return bossTemplateMap.getOrDefault(id, null);
    }

    /**
     * 获取赠送比例
     *
     * @param value
     * @return
     */
    public double getTreasureRate(int value) {
        ActiveSpringTreasureRateTemplateImpl cfg = treasureRateTemplateList.stream().
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
        ActiveSpringRateFirstTemplateImpl cfg = rateFirstTemplateList.stream().
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

    /**
     * 获取年兽爆率
     *
     * @param times
     * @return
     */
    public int getBossDamageRate(int times) {
        int result = 1;
        Optional<ActiveSpringBossDamageTemplateImpl> first = bossDamageTemplateList.stream().filter(e -> e.getDown() <= times && times <= e.getUp()).findFirst();
        if (first.isPresent()) {
            return first.get().getRate();
        }

        // 次数超过表里配置上限，则取模继续
        int maxTimes = bossDamageTemplateList.stream().map(ActiveSpringBossDamageTemplateImpl::getUp).max(Integer::compareTo).orElse(0);
        if (maxTimes <= 0) {
            return result;
        }
        int newTimes = times % maxTimes;
        ActiveSpringBossDamageTemplateImpl template = bossDamageTemplateList.stream().filter(e -> e.getDown() <= newTimes && newTimes <= e.getUp()).findFirst().orElse(null);
        if (template != null) {
            return template.getRate();
        }
        return result;
    }

    @Override
    public void loadConfig() {
        /*List<ActiveSpringBossTemplate> l1 = JSONUtil.fromJson(getJson(ActiveSpringBossTemplate.class), new TypeReference<List<ActiveSpringBossTemplate>>() {
        });
        Map<Integer, ActiveSpringBossTemplate> m1 = l1.stream().collect(Collectors.toMap(ActiveSpringBossTemplate::getId, Function.identity()));
        bossTemplateMap = ImmutableMap.copyOf(m1);
        // ========================================================
        List<ActiveSpringCardTemplateImpl> l2 = JSONUtil.fromJson(getJson(ActiveSpringCardTemplateImpl.class), new TypeReference<List<ActiveSpringCardTemplateImpl>>() {
        });
        Map<Integer, ActiveSpringCardTemplateImpl> m2 = Maps.newConcurrentMap();
        l2.forEach(t -> {
            t.init();
            m2.put(t.getId(), t);
        });
        cardTemplateMap = ImmutableMap.copyOf(m2);
        // ========================================================
        List<ActiveSpringCardRewardTemplateImpl> l3 = JSONUtil.fromJson(getJson(ActiveSpringCardRewardTemplateImpl.class), new TypeReference<List<ActiveSpringCardRewardTemplateImpl>>() {
        });
        Map<Integer, ActiveSpringCardRewardTemplateImpl> m3 = Maps.newConcurrentMap();
        l3.forEach(t -> {
            t.init();
            m3.put(t.getId(), t);
        });
        cardRewardTemplateMap = ImmutableMap.copyOf(m3);
        // ========================================================
        List<ActiveSpringBossRewardTemplateImpl> l4 = JSONUtil.fromJson(getJson(ActiveSpringBossRewardTemplateImpl.class), new TypeReference<List<ActiveSpringBossRewardTemplateImpl>>() {
        });
        Map<Integer, ActiveSpringBossRewardTemplateImpl> m4 = Maps.newConcurrentMap();
        l4.forEach(t -> {
            t.init();
            m4.put(t.getId(), t);
        });
        bossRewardTemplateMap = ImmutableMap.copyOf(m4);
        // ========================================================
        List<ActiveSpringRechargeBackTemplateImpl> l5 = JSONUtil.fromJson(getJson(ActiveSpringRechargeBackTemplateImpl.class), new TypeReference<List<ActiveSpringRechargeBackTemplateImpl>>() {
        });
        Map<Integer, ActiveSpringRechargeBackTemplateImpl> m5 = Maps.newConcurrentMap();
        l5.forEach(t -> {
            t.init();
            m5.put(t.getId(), t);
        });
        rechargeBackTemplateMap = ImmutableMap.copyOf(m5);
        // ========================================================
        List<ActiveSpringRateFirstTemplateImpl> l6 = JSONUtil.fromJson(getJson(ActiveSpringRateFirstTemplateImpl.class), new TypeReference<List<ActiveSpringRateFirstTemplateImpl>>() {
        });
        l6.forEach(ActiveSpringRateFirstTemplateImpl::init);
        rateFirstTemplateList = ImmutableList.copyOf(l6);
        // ========================================================
        List<ActiveSpringTreasureRateTemplateImpl> l7 = JSONUtil.fromJson(getJson(ActiveSpringTreasureRateTemplateImpl.class), new TypeReference<List<ActiveSpringTreasureRateTemplateImpl>>() {
        });
        l7.forEach(ActiveSpringTreasureRateTemplateImpl::init);
        treasureRateTemplateList = ImmutableList.copyOf(l7);
        // ========================================================
        List<ActiveSpringBossDamageTemplateImpl> l8 = JSONUtil.fromJson(getJson(ActiveSpringBossDamageTemplateImpl.class), new TypeReference<List<ActiveSpringBossDamageTemplateImpl>>() {
        });
        l8.forEach(ActiveSpringBossDamageTemplateImpl::init);
        bossDamageTemplateList = ImmutableList.copyOf(l8);*/

    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveSpringBossRewardTemplateImpl.class, ActiveSpringCardRewardTemplateImpl.class,
                ActiveSpringCardTemplateImpl.class, ActiveSpringRateFirstTemplateImpl.class,
                ActiveSpringRechargeBackTemplateImpl.class, ActiveSpringTreasureRateTemplateImpl.class,
                ActiveSpringBossTemplate.class, ActiveSpringBossDamageTemplateImpl.class
        );
    }

}
