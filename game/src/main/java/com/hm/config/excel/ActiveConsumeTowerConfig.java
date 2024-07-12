package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveConsumeTowerTemplate;
import com.hm.config.excel.templaextra.ActiveConsumeTowerTemplateImpl;
import com.hm.util.RandomUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
/**
 * @description: 消费宝塔
 * @author: chenwei
 * @create: 2020-03-13 15:28
 **/
@Config
public class ActiveConsumeTowerConfig extends ExcleConfig{

    private int maxLevel;
    private Map<Integer, ActiveConsumeTowerTemplateImpl> consumeTowerMap = Maps.newConcurrentMap();
    private ArrayListMultimap<Integer, ActiveConsumeTowerTemplateImpl> consumeTowerMutimap = ArrayListMultimap.create();
//    private Table<Integer,Integer,ActiveConsumeTowerTemplateImpl> levelTimeConsumeTowerTable = HashBasedTable.create();

    @Override
    public void loadConfig() {
       /* Map<Integer, ActiveConsumeTowerTemplateImpl> templateMap = Maps.newHashMap();
//        Table<Integer,Integer,ActiveConsumeTowerTemplateImpl> tempTable = HashBasedTable.create();
        ArrayListMultimap<Integer, ActiveConsumeTowerTemplateImpl> templateMultimap = ArrayListMultimap.create();
        List<ActiveConsumeTowerTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveConsumeTowerTemplateImpl.class), new TypeReference<List<ActiveConsumeTowerTemplateImpl>>() {});
        list.forEach(e -> {
            e.init();
            templateMultimap.put(e.getLevel(), e);
//            if (e.getBottom_protection() > 0){
//                tempTable.put(e.getLevel(),e.getBottom_protection(), e);
//            }
        });
        templateMap = list.stream().collect(Collectors.toMap(ActiveConsumeTowerTemplate::getId, Function.identity()));
        maxLevel = list.stream().map(ActiveConsumeTowerTemplate::getLevel).sorted(Comparator.comparing(Integer::intValue).reversed()).findFirst().orElse(0);
        consumeTowerMap = ImmutableMap.copyOf(templateMap);
        consumeTowerMutimap = templateMultimap;*/
//        levelTimeConsumeTowerTable = ImmutableTable.copyOf(tempTable);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveConsumeTowerTemplate.class);
    }

    public ActiveConsumeTowerTemplateImpl getActiveConsumeTowerTemplate(int id){
        return consumeTowerMap.get(id);
    }

    public List<ActiveConsumeTowerTemplateImpl> getActiveConsumeTowerListByLevel(int level){
        return consumeTowerMutimap.get(level);
    }

    public ActiveConsumeTowerTemplateImpl randomConsumeTowerTemplate(int level, int playerLv, int times){
        List<ActiveConsumeTowerTemplateImpl> list = getActiveConsumeTowerListByLevel(level);
        if (CollUtil.isNotEmpty(list)){
            Map<ActiveConsumeTowerTemplateImpl,Integer> wm = Maps.newHashMap();
            List<ActiveConsumeTowerTemplateImpl> collect = list.stream().filter(e -> e.isFit(playerLv))
                    .filter(e -> e.getNumber() <= times).collect(Collectors.toList());
            for(ActiveConsumeTowerTemplateImpl template : collect){
                wm.put(template, template.getRate());
                if (template.getBottom_protection() == times){
                    return template;
                }
            }
            return RandomUtils.buildWeightMeta(wm).random();
        }
        return null;
    }

    public int getMaxLevel(){
       return maxLevel;
    }

}
