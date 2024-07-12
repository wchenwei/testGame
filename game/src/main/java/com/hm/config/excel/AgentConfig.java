package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.AgentCenterUpgradeTemplate;
import com.hm.config.excel.temlate.AgentLevelUpTemplate;
import com.hm.config.excel.templaextra.AgentBaseTemplateImpl;
import com.hm.config.excel.templaextra.AgentCenterUnlockExtraTemplate;
import com.hm.config.excel.templaextra.AgentCenterUpgradeExtraTemplate;
import com.hm.config.excel.templaextra.AgentFeedbackTemplateImpl;
import com.hm.enums.TankAttrType;
import com.hm.model.tank.TankAttr;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2019-07-08
 */
@Config
public class AgentConfig extends ExcleConfig {
    /**
     * key:AgentBaseTemplateImpl::getId
     */
    private Map<Integer, AgentBaseTemplateImpl> baseMap = Maps.newConcurrentMap();

    /**
     * key:AgentLevelUpTemplate::getLevel
     */
    private Map<Integer, AgentLevelUpTemplate> levelUpMap = Maps.newConcurrentMap();
    /**
     * key:AgentFeedbackTemplateImpl::getIntimacy
     */
    private Map<Integer, AgentFeedbackTemplateImpl> feedbackMap = Maps.newConcurrentMap();
    private int agentCenterMaxLv;

    public AgentBaseTemplateImpl getBaseCfg(int agentId) {
        return baseMap.getOrDefault(agentId, null);
    }

    // 特工中心
    private Map<Integer, AgentCenterUpgradeExtraTemplate> agentCenterUpgrade = Maps.newConcurrentMap();
    private Map<Integer, AgentCenterUnlockExtraTemplate> agentCenterUnlock = Maps.newConcurrentMap();

    /**
     * 坦克属性
     *
     * @param agentId
     * @return
     */
    public Map<TankAttrType, Double> getTankAttrMap(int agentId) {
        if (baseMap.containsKey(agentId)) {
            return baseMap.get(agentId).getTankAttrMap();
        }
        return Maps.newConcurrentMap();
    }

    /**
     * 全体属性
     *
     * @param agentId
     * @param agentLv
     * @return
     */
    public Map<TankAttrType, Double> getTotalAttrMap(int agentId, int agentLv) {
        // 等级跟1级的差值
        int lvAdd = agentLv - 1;
        Map<TankAttrType, Double> map = Maps.newConcurrentMap();
        if (baseMap.containsKey(agentId)) {
            Map<TankAttrType, Pair<Double, Double>> pairMap = baseMap.get(agentId).getTotalAttrPairMap();
            pairMap.forEach((key, value) -> map.put(key, value.getFirst() + value.getSecond() * lvAdd));
        }
        return map;
    }

    public AgentLevelUpTemplate getLevelUpCfg(int lv) {
        return levelUpMap.getOrDefault(lv, null);
    }

    /**
     * 根据经验算等级
     *
     * @param exp
     * @return
     */
    public int calcAgentLv(int exp) {
        return levelUpMap.values().stream().filter(e -> e.getTotal_exp() <= exp).mapToInt(AgentLevelUpTemplate::getLevel).max().orElse(0);
    }

    public AgentFeedbackTemplateImpl getFeedbackCfg(int id) {
        return feedbackMap.getOrDefault(id, null);
    }

    public int getAgentCenterMaxLv() {
        return agentCenterMaxLv;
    }
    public AgentCenterUpgradeExtraTemplate getAgentCenterUpgradeCfg(int lv) {
        return agentCenterUpgrade.getOrDefault(lv, null);
    }
    //获取等级带来的属性加成
    public TankAttr getAgentCenterExtraAttr(int lv) {
        TankAttr tankAttr = new TankAttr();
        agentCenterUnlock.values().stream().filter(t ->lv>=t.getUnlock_lv()).forEach(t ->{
            tankAttr.addAttr(t.getAttrMap());
        });
        return tankAttr;
    }

    @Override
    public void loadConfig() {
        // =============================================================================================================
        List<AgentBaseTemplateImpl> list = JSONUtil.fromJson(getJson(AgentBaseTemplateImpl.class), new TypeReference<List<AgentBaseTemplateImpl>>() {
        });

        Map<Integer, AgentBaseTemplateImpl> tmpAgentBaseTemplateMap = Maps.newConcurrentMap();
        list.forEach(t -> {
            t.init();
            tmpAgentBaseTemplateMap.put(t.getId(), t);
        });

        baseMap = ImmutableMap.copyOf(tmpAgentBaseTemplateMap);

        // =============================================================================================================
        List<AgentLevelUpTemplate> list1 = JSONUtil.fromJson(getJson(AgentLevelUpTemplate.class), new TypeReference<List<AgentLevelUpTemplate>>() {
        });

        Map<Integer, AgentLevelUpTemplate> tmpLevelUpMap = list1.stream().collect(Collectors.toMap(AgentLevelUpTemplate::getLevel, Function.identity()));
        levelUpMap = ImmutableMap.copyOf(tmpLevelUpMap);

        // =============================================================================================================
        List<AgentFeedbackTemplateImpl> list2 = JSONUtil.fromJson(getJson(AgentFeedbackTemplateImpl.class), new TypeReference<List<AgentFeedbackTemplateImpl>>() {
        });

        Map<Integer, AgentFeedbackTemplateImpl> tmpFeedbackMap = Maps.newConcurrentMap();
        list2.forEach(t -> {
            t.init();
            tmpFeedbackMap.put(t.getId(), t);
        });
        feedbackMap = ImmutableMap.copyOf(tmpFeedbackMap);

        loadAgentCenterUnlockConfig();
        loadAgentCenterUpgradeConfig();
    }

    private void loadAgentCenterUnlockConfig() {
        List<AgentCenterUnlockExtraTemplate> list = JSONUtil.fromJson(getJson(AgentCenterUnlockExtraTemplate.class), new TypeReference<ArrayList<AgentCenterUnlockExtraTemplate>>() {
        });
        list.forEach(AgentCenterUnlockExtraTemplate::init);
        Map<Integer, AgentCenterUnlockExtraTemplate> m = list.stream().collect(Collectors.toMap(AgentCenterUnlockExtraTemplate::getId, Function.identity()));
        this.agentCenterUnlock = ImmutableMap.copyOf(m);

    }

    private void loadAgentCenterUpgradeConfig() {
        List<AgentCenterUpgradeExtraTemplate> list = JSONUtil.fromJson(getJson(AgentCenterUpgradeExtraTemplate.class), new TypeReference<ArrayList<AgentCenterUpgradeExtraTemplate>>() {
        });
        list.forEach(AgentCenterUpgradeExtraTemplate::init);
        Map<Integer, AgentCenterUpgradeExtraTemplate> m = list.stream().collect(Collectors.toMap(AgentCenterUpgradeExtraTemplate::getLevel, Function.identity()));
        this.agentCenterUpgrade = ImmutableMap.copyOf(m);
        this.agentCenterMaxLv = list.stream().mapToInt(AgentCenterUpgradeTemplate::getLevel).max().orElse(0);
    }
    
    @Override
    public List<String> getDownloadFile() {
        return getConfigName(AgentBaseTemplateImpl.class, AgentLevelUpTemplate.class, AgentFeedbackTemplateImpl.class,
                AgentCenterUnlockExtraTemplate.class, AgentCenterUpgradeExtraTemplate.class);
    }
}
