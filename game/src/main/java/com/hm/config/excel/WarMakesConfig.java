package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.KfSeasonPassGiftTemplate;
import com.hm.config.excel.temlate.KfSeasonPassLevelTemplate;
import com.hm.config.excel.temlate.KfSeasonPassTemplate;
import com.hm.config.excel.templaextra.KfSeasonPassTemplateImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 *          赛季战令
 * @date 2021/4/14 20:28
 */
@Config
public class WarMakesConfig extends ExcleConfig{

    private Map<Integer, KfSeasonPassTemplateImpl> passTemplateMap = Maps.newHashMap();

    private Map<Integer, KfSeasonPassGiftTemplate> passGiftTemplateMap = Maps.newHashMap();

    private List<KfSeasonPassLevelTemplate> passLevelTemplateList = Lists.newArrayList();

    private int maxStage;

    public KfSeasonPassTemplateImpl getKfSeasonPassTemplate(int stage, int id){
        KfSeasonPassTemplateImpl kfSeasonPassTemplate = passTemplateMap.values().stream()
                .filter(e -> e.getStage() == stage && e.getId() == id).findFirst().orElse(null);
        return kfSeasonPassTemplate;
    }

    public List<KfSeasonPassTemplateImpl> receiveAll(int stage, int level){
        List<KfSeasonPassTemplateImpl> collect = passTemplateMap.values().stream()
                .filter(e ->e.getStage() == stage && level >= e.getPass_level()).collect(Collectors.toList());
        return collect;
    }

    public KfSeasonPassGiftTemplate getGiftTemplate(int giftId){
        KfSeasonPassGiftTemplate kfSeasonPassGiftTemplate = passGiftTemplateMap.get(giftId);
        return kfSeasonPassGiftTemplate;
    }

    public int getLevel(int experience){
        KfSeasonPassLevelTemplate kfSeasonPassLevelTemplate = passLevelTemplateList.stream()
                .sorted(Comparator.comparingInt(KfSeasonPassLevelTemplate::getPass_exp_total).reversed())
                .filter(e -> experience >= e.getPass_exp_total()).findFirst().orElse(null);
        return kfSeasonPassLevelTemplate == null ? 0:kfSeasonPassLevelTemplate.getId();
    }

    public int getStage(int seasonId){
        int stage = seasonId % 6;
        return stage > 0 ? stage : 6;
    }

    @Override
    public void loadConfig() {
//        loadKfSeasonPass();
//        loadSeasonPassGift();
//        loadSeasonPassLevel();
    }

    private void loadSeasonPassLevel() {
        List<KfSeasonPassLevelTemplate> list = JSONUtil.fromJson(getJson(KfSeasonPassLevelTemplate.class), new TypeReference<List<KfSeasonPassLevelTemplate>>() {
        });
        passLevelTemplateList = ImmutableList.copyOf(list);
    }

    private void loadSeasonPassGift() {
        List<KfSeasonPassGiftTemplate> list = JSONUtil.fromJson(getJson(KfSeasonPassGiftTemplate.class), new TypeReference<List<KfSeasonPassGiftTemplate>>() {
        });
        Map<Integer, KfSeasonPassGiftTemplate> tempMap = list.stream().collect(Collectors.toMap(KfSeasonPassGiftTemplate::getRecharge_gift_id, Function.identity()));
        passGiftTemplateMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadKfSeasonPass() {
        List<KfSeasonPassTemplateImpl> list = JSONUtil.fromJson(getJson(KfSeasonPassTemplateImpl.class), new TypeReference<List<KfSeasonPassTemplateImpl>>() {
        });
        list.forEach(e -> {
            e.init();
        });
        maxStage = list.stream().max(Comparator.comparing(KfSeasonPassTemplate::getStage)).get().getStage();
        Map<Integer, KfSeasonPassTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(KfSeasonPassTemplateImpl::getId, Function.identity()));
        passTemplateMap = ImmutableMap.copyOf(tempMap);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(KfSeasonPassTemplateImpl.class, KfSeasonPassGiftTemplate.class, KfSeasonPassLevelTemplate.class);
    }
}
