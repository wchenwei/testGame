package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active0909BossTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Config
public class ActivityDoubleNinthConfig extends ExcleConfig {
    /**
     * Active0909BossTemplate::getId : Active0909BossTemplate
     */
    private Map<Integer, Active0909BossTemplate> bossTemplateMap = Maps.newConcurrentMap();
    /**
     * Active0909BossRewardTemplateImpl::getId : Active0909BossRewardTemplateImpl
     */
    private Map<Integer, Active0909BossRewardTemplateImpl> bossRewardTemplateMap = Maps.newConcurrentMap();
    /**
     * Active0909CardTemplateImpl::getId : Active0909CardTemplateImpl
     */
    private Map<Integer, Active0909CardTemplateImpl> cardTemplateMap = Maps.newConcurrentMap();
    /**
     * Active0909CardRewardTemplateImpl::getId : Active0909CardRewardTemplateImpl
     */
    private Map<Integer, Active0909CardRewardTemplateImpl> cardRewardTemplateMap = Maps.newConcurrentMap();
    /**
     * Active0909RechargeBackTemplateImpl::getId : Active0909RechargeBackTemplateImpl
     */
    private Map<Integer, Active0909RechargeBackTemplateImpl> rechargeBackTemplateMap = Maps.newConcurrentMap();
    private List<Active0909RateFirstTemplateImpl> rateFirstTemplateList = Lists.newArrayList();
    private List<Active0909TreasureRateTemplateImpl> treasureRateTemplateList = Lists.newArrayList();
    private List<Active0909BossDamageTemplateImpl> bossDamageTemplateList = Lists.newArrayList();
    private Map<Integer, Active0909StageTemplateImpl> stageMap = Maps.newConcurrentMap();

    public Active0909CardTemplateImpl getCardCfg(int id_sub, int stage) {
        return cardTemplateMap.values().stream().filter(e->e.getStage().equals(stage) && e.getId_sub().equals(id_sub)).findAny().orElse(null);
    }

    public Active0909CardRewardTemplateImpl getCardRewardCfg(int id) {
        return cardRewardTemplateMap.getOrDefault(id, null);
    }

    /**
     * 根据vip point 获取 配置信息
     *
     * @param point
     * @param stage
     * @return
     */
    public Active0909RechargeBackTemplateImpl queryByVipPoint(int point, int stage) {
        Optional<Active0909RechargeBackTemplateImpl> first = rechargeBackTemplateMap.values().stream()
                .filter(e -> e.getStage().equals(stage) && e.getCharge_gold().equals(point)).findFirst();
        return first.orElse(null);
    }

    public Active0909RechargeBackTemplateImpl getRechargeBackCfg(int id) {
        return rechargeBackTemplateMap.getOrDefault(id, null);
    }


    public Active0909BossRewardTemplateImpl getBossRewardCfg(int bossLv, int playerLv, int stage) {
        Active0909BossRewardTemplateImpl cfg = bossRewardTemplateMap.values().stream().filter(e -> e.getStage().equals(stage) && e.getBoss_lv().equals(bossLv))
                .filter(e -> e.getPlayer_lv_down() <= playerLv && playerLv <= e.getPlayer_lv_up()).findFirst().orElse(null);
        return cfg;
    }

    public Active0909BossTemplate getBossCfg(int id) {
        return bossTemplateMap.getOrDefault(id, null);
    }

    /**
     * 获取赠送比例
     *
     * @param value
     * @return
     */
    public double getTreasureRate(int value) {
        Active0909TreasureRateTemplateImpl cfg = treasureRateTemplateList.stream().
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
        Active0909RateFirstTemplateImpl cfg = rateFirstTemplateList.stream().
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
        Optional<Active0909BossDamageTemplateImpl> first = bossDamageTemplateList.stream().filter(e -> e.getDown() <= times && times <= e.getUp()).findFirst();
        if (first.isPresent()) {
            return first.get().getRate();
        }

        // 次数超过表里配置上限，则取模继续
        int maxTimes = bossDamageTemplateList.stream().map(Active0909BossDamageTemplateImpl::getUp).max(Integer::compareTo).orElse(0);
        if (maxTimes <= 0) {
            return result;
        }
        int newTimes = times % maxTimes;
        Active0909BossDamageTemplateImpl template = bossDamageTemplateList.stream().filter(e -> e.getDown() <= newTimes && newTimes <= e.getUp()).findFirst().orElse(null);
        if (template != null) {
            return template.getRate();
        }
        return result;
    }


    public Active0909StageTemplateImpl getStageCfg(int stage) {
        return stageMap.getOrDefault(stage, null);
    }

    @Override
    public void loadConfig() {
        /*List<Active0909BossTemplate> l1 = JSONUtil.fromJson(getJson(Active0909BossTemplate.class), new TypeReference<List<Active0909BossTemplate>>() {
        });
        Map<Integer, Active0909BossTemplate> m1 = l1.stream().collect(Collectors.toMap(Active0909BossTemplate::getId, Function.identity()));
        bossTemplateMap = ImmutableMap.copyOf(m1);
        // ========================================================
        List<Active0909CardTemplateImpl> l2 = JSONUtil.fromJson(getJson(Active0909CardTemplateImpl.class), new TypeReference<List<Active0909CardTemplateImpl>>() {
        });
        Map<Integer, Active0909CardTemplateImpl> m2 = Maps.newConcurrentMap();
        l2.forEach(t -> {
            t.init();
            m2.put(t.getId(), t);
        });
        cardTemplateMap = ImmutableMap.copyOf(m2);
        // ========================================================
        List<Active0909CardRewardTemplateImpl> l3 = JSONUtil.fromJson(getJson(Active0909CardRewardTemplateImpl.class), new TypeReference<List<Active0909CardRewardTemplateImpl>>() {
        });
        Map<Integer, Active0909CardRewardTemplateImpl> m3 = Maps.newConcurrentMap();
        l3.forEach(t -> {
            t.init();
            m3.put(t.getId(), t);
        });
        cardRewardTemplateMap = ImmutableMap.copyOf(m3);
        // ========================================================
        List<Active0909BossRewardTemplateImpl> l4 = JSONUtil.fromJson(getJson(Active0909BossRewardTemplateImpl.class), new TypeReference<List<Active0909BossRewardTemplateImpl>>() {
        });
        Map<Integer, Active0909BossRewardTemplateImpl> m4 = Maps.newConcurrentMap();
        l4.forEach(t -> {
            t.init();
            m4.put(t.getId(), t);
        });
        bossRewardTemplateMap = ImmutableMap.copyOf(m4);
        // ========================================================
        List<Active0909RechargeBackTemplateImpl> l5 = JSONUtil.fromJson(getJson(Active0909RechargeBackTemplateImpl.class), new TypeReference<List<Active0909RechargeBackTemplateImpl>>() {
        });
        Map<Integer, Active0909RechargeBackTemplateImpl> m5 = Maps.newConcurrentMap();
        l5.forEach(t -> {
            t.init();
            m5.put(t.getId(), t);
        });
        rechargeBackTemplateMap = ImmutableMap.copyOf(m5);
        // ========================================================
        List<Active0909RateFirstTemplateImpl> l6 = JSONUtil.fromJson(getJson(Active0909RateFirstTemplateImpl.class), new TypeReference<List<Active0909RateFirstTemplateImpl>>() {
        });
        l6.forEach(Active0909RateFirstTemplateImpl::init);
        rateFirstTemplateList = ImmutableList.copyOf(l6);
        // ========================================================
        List<Active0909TreasureRateTemplateImpl> l7 = JSONUtil.fromJson(getJson(Active0909TreasureRateTemplateImpl.class), new TypeReference<List<Active0909TreasureRateTemplateImpl>>() {
        });
        l7.forEach(Active0909TreasureRateTemplateImpl::init);
        treasureRateTemplateList = ImmutableList.copyOf(l7);
        // ========================================================
        List<Active0909BossDamageTemplateImpl> l8 = JSONUtil.fromJson(getJson(Active0909BossDamageTemplateImpl.class), new TypeReference<List<Active0909BossDamageTemplateImpl>>() {
        });
        l8.forEach(Active0909BossDamageTemplateImpl::init);
        bossDamageTemplateList = ImmutableList.copyOf(l8);
        // ========================================================
        List<Active0909StageTemplateImpl> l9 = JSONUtil.fromJson(getJson(Active0909StageTemplateImpl.class), new TypeReference<List<Active0909StageTemplateImpl>>() {
        });
        Map<Integer, Active0909StageTemplateImpl> m9 = Maps.newConcurrentMap();
        l9.forEach(t -> {
            t.init();
            m9.put(t.getId(), t);
        });
        stageMap = ImmutableMap.copyOf(m9);*/

    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active0909BossRewardTemplateImpl.class,
                Active0909CardRewardTemplateImpl.class,
                Active0909CardTemplateImpl.class,
                Active0909RateFirstTemplateImpl.class,
                Active0909RechargeBackTemplateImpl.class,
                Active0909TreasureRateTemplateImpl.class,
                Active0909BossTemplate.class,
                Active0909BossDamageTemplateImpl.class,
                Active0909StageTemplateImpl.class
        );
    }

}
