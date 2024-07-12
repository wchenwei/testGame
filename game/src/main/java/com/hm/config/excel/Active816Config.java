package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active816GiftTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.model.item.Items;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Config
public class Active816Config extends ExcleConfig {
    private Map<Integer, Active816RedEnvelopesTemplateImpl> redMap = Maps.newConcurrentMap();
    private Map<Integer, Active816GiftTemplate> giftMap = Maps.newConcurrentMap();
    private Map<Integer, Active816LoginTemplateImpl> loginMap = Maps.newConcurrentMap();
    private Map<Integer, Active816FormulaImpl> formulaMap = Maps.newConcurrentMap();

    private Map<Integer, Active816GuessrewardTemplateImpl> guessRewardTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, Active816GuessTemplateImpl> guessTemplateMap = Maps.newConcurrentMap();

    public Active816LoginTemplateImpl getLoginCfg(int id) {
        return loginMap.getOrDefault(id, null);
    }

    public Active816FormulaImpl getFormulaCfg(int id) {
        return formulaMap.getOrDefault(id, null);
    }

    /**
     * 获取活动期间该玩家可见gift id list
     *
     * @param stage
     * @param playerLv
     * @return
     */
    public Set<Integer> getGiftIds(int stage, int playerLv) {
        return giftMap.values().stream().filter(e -> e.getStage().equals(stage)).
                filter(e -> e.getPlayer_lv_down() <= playerLv && playerLv <= e.getPlayer_lv_up()).
                map(Active816GiftTemplate::getRecharge_gift_id).collect(Collectors.toSet());
    }

    public Active816RedEnvelopesTemplateImpl getRedCfg(int serverLv) {
        Optional<Active816RedEnvelopesTemplateImpl> first = redMap.values().stream().
                filter(e -> e.getServer_level_low() <= serverLv && serverLv <= e.getServer_level_high()).findFirst();
        return first.orElse(null);
    }

    public List<Items> getGuessReward(int stage, int playerLv, List<Integer> successIds) {
        List<Items> list = guessTemplateMap.values().stream()
                .filter(e -> e.isFit(playerLv, stage))
                .filter(e -> CollUtil.contains(successIds, e.getId()))
                .flatMap(e -> e.getRewards().stream())
                .collect(Collectors.toList());
        Active816GuessrewardTemplateImpl active816GuessrewardTemplate = guessRewardTemplateMap.values().stream().filter(e -> e.isFitRule(playerLv, successIds.size(), stage)).findFirst().orElse(null);
        if(active816GuessrewardTemplate != null){
            list.addAll(active816GuessrewardTemplate.getRewards());
        }
        return list;
    }

    @Override
    public void loadConfig() {
//        log.info("Active816Config.loadConfig begin");
//        /////////////
//        List<Active816RedEnvelopesTemplateImpl> list = JSONUtil.fromJson(getJson(Active816RedEnvelopesTemplateImpl.class), new TypeReference<List<Active816RedEnvelopesTemplateImpl>>() {
//        });
//
//        Map<Integer, Active816RedEnvelopesTemplateImpl> m = Maps.newConcurrentMap();
//        list.forEach(template -> {
//            template.init();
//            m.put(template.getId(), template);
//        });
//        redMap = ImmutableMap.copyOf(m);
//        /////////////
//        List<Active816GiftTemplate> list1 = JSONUtil.fromJson(getJson(Active816GiftTemplate.class), new TypeReference<List<Active816GiftTemplate>>() {
//        });
//
//        Map<Integer, Active816GiftTemplate> m1 = Maps.newConcurrentMap();
//        list1.forEach(template -> {
//            m1.put(template.getId(), template);
//        });
//        giftMap = ImmutableMap.copyOf(m1);
//        /////////////
//        List<Active816LoginTemplateImpl> list2 = JSONUtil.fromJson(getJson(Active816LoginTemplateImpl.class), new TypeReference<List<Active816LoginTemplateImpl>>() {
//        });
//
//        Map<Integer, Active816LoginTemplateImpl> m2 = Maps.newConcurrentMap();
//        list2.forEach(template -> {
//            template.init();
//            m2.put(template.getId(), template);
//        });
//        loginMap = ImmutableMap.copyOf(m2);
//        /////////////
//        List<Active816FormulaImpl> list3 = JSONUtil.fromJson(getJson(Active816FormulaImpl.class), new TypeReference<List<Active816FormulaImpl>>() {
//        });
//
//        Map<Integer, Active816FormulaImpl> m3 = Maps.newConcurrentMap();
//        list3.forEach(template -> {
//            template.init();
//            m3.put(template.getId(), template);
//        });
//        formulaMap = ImmutableMap.copyOf(m3);
//
//        loadGuessRewardTemplate();
//        loadGuessTemplate();
//        log.info("Active816Config.loadConfig end");
    }

    private void loadGuessRewardTemplate() {
        Map<Integer, Active816GuessrewardTemplateImpl> tempBuyGiftTemplate = Maps.newConcurrentMap();
        for(Active816GuessrewardTemplateImpl template :JSONUtil.fromJson(getJson(Active816GuessrewardTemplateImpl.class),
                new TypeReference<ArrayList<Active816GuessrewardTemplateImpl>>() {
                })) {
            template.init();
            tempBuyGiftTemplate.put(template.getId(), template);
        }
        this.guessRewardTemplateMap  = ImmutableMap.copyOf(tempBuyGiftTemplate);
    }

    private void loadGuessTemplate() {
        Map<Integer, Active816GuessTemplateImpl> tempBuyGiftTemplate = Maps.newConcurrentMap();
        for(Active816GuessTemplateImpl template :JSONUtil.fromJson(getJson(Active816GuessTemplateImpl.class),
                new TypeReference<ArrayList<Active816GuessTemplateImpl>>() {
                })) {
            template.init();
            tempBuyGiftTemplate.put(template.getId(), template);
        }
        this.guessTemplateMap  = ImmutableMap.copyOf(tempBuyGiftTemplate);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active816FormulaImpl.class, Active816GiftTemplate.class,
                Active816LoginTemplateImpl.class, Active816RedEnvelopesTemplateImpl.class,
        Active816GuessrewardTemplateImpl.class, Active816GuessTemplateImpl.class);
    }
}
