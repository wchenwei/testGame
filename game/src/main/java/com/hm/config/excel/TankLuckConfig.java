package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.*;
import com.hm.config.excel.temlate.LuckyBaseTemplate;
import com.hm.config.excel.temlate.LuckyPondTemplate;
import com.hm.config.excel.templaextra.LuckyPondTemplateImpl;
import com.hm.enums.LuckPondType;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.util.weight.WeightRandom;
import com.hm.model.item.Items;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Config
public class TankLuckConfig extends ExcleConfig{

    private Map<Integer, LuckyBaseTemplate> luckyBaseMap = Maps.newHashMap();
    private Map<Integer, LuckyPondTemplateImpl> luckyPondMap = Maps.newConcurrentMap();

    // id - weight
    private Map<Integer, Integer> baseWeightMap = Maps.newHashMap();
    private ListMultimap<Integer,Integer> pondTypeIdMap = ArrayListMultimap.create();


    private List<LuckyBaseTemplate> addWeightTemplates= Lists.newArrayList();

    Map<Integer, WeightRandom<Items>> weightItemMap = Maps.newHashMap();


    @Override
    public void loadConfig() {
        loadBaseLuckConfig();
        loadLuckPondConfig();
    }


    private void loadBaseLuckConfig() {
        List<LuckyBaseTemplate> luckyBaseTemplates = json2ImmutableList(LuckyBaseTemplate.class);
        Map<Integer, LuckyBaseTemplate> baseTemplateMap = Maps.newHashMap();
        Map<Integer, Integer> temWeightMap = Maps.newConcurrentMap();
        List<LuckyBaseTemplate> addWeightTemplates= Lists.newArrayList();
        ListMultimap<Integer,Integer> pondTypeIdMap = ArrayListMultimap.create();
        for (LuckyBaseTemplate template : luckyBaseTemplates) {
            baseTemplateMap.put(template.getId(), template);
            temWeightMap.put(template.getId(), template.getWeight());
            pondTypeIdMap.put(template.getType(), template.getId());
            if (template.getWeight_add() > 0){
                addWeightTemplates.add(template);
            }
        }
        this.luckyBaseMap = ImmutableMap.copyOf(baseTemplateMap);
        this.baseWeightMap = ImmutableMap.copyOf(temWeightMap);
        this.addWeightTemplates = ImmutableList.copyOf(addWeightTemplates);
        this.pondTypeIdMap = ImmutableListMultimap.copyOf(pondTypeIdMap);
    }


    private void loadLuckPondConfig() {
        List<LuckyPondTemplateImpl> pondTemplates = json2ImmutableList(LuckyPondTemplateImpl.class);
        Map<Integer, LuckyPondTemplateImpl> templateMap = pondTemplates.stream().collect(Collectors.toMap(LuckyPondTemplate::getId, Function.identity()));
        this.luckyPondMap = ImmutableMap.copyOf(templateMap);

        Map<Integer, WeightRandom<Items>> weightItemMap = Maps.newHashMap();
        Map<Integer, List<LuckyPondTemplateImpl>> listMap = pondTemplates.stream().collect(Collectors.groupingBy(LuckyPondTemplate::getPond_id));
        for (Map.Entry<Integer, List<LuckyPondTemplateImpl>> entry : listMap.entrySet()) {
            Integer pondId = entry.getKey();
            List<LuckyPondTemplateImpl> templates = entry.getValue();
            Map<Items, Integer> weightMap = Maps.newHashMap();
            templates.forEach(e -> weightMap.put(e.getRewards(), e.getWeight()));
            weightItemMap.put(pondId, RandomUtils.buildWeightRandom(weightMap));
        }
        this.weightItemMap = ImmutableMap.copyOf(weightItemMap);

    }

    public LuckyBaseTemplate getLuckyBaseTemplate(int id){
        return luckyBaseMap.get(id);
    }

    public LuckyBaseTemplate randomLuckTemplate(LuckPondType pondType, Map<Integer, Integer> addWeightMap){
        Map<Integer, Integer> weightMap = getPondWeightMap(pondType, addWeightMap);
        Integer id = RandomUtils.buildWeightRandom(weightMap).next();
        return luckyBaseMap.get(id);
    }

    // 获取池子权重
    private Map<Integer, Integer> getPondWeightMap(LuckPondType pondType, Map<Integer, Integer> addWeightMap){
        Map<Integer, Integer> map = Maps.newHashMap();
        List<Integer> pondIds = pondTypeIdMap.get(pondType.getType());
        if (CollUtil.isNotEmpty(pondIds)){
            for (Integer pondId : pondIds) {
                int newWeight = baseWeightMap.get(pondId) + addWeightMap.getOrDefault(pondId, 0);
                map.put(pondId, newWeight);
            }
        }
        return map;
    }

    public List<LuckyBaseTemplate> getAddWeightTemplates() {
        return addWeightTemplates;
    }

    public Items randItems(int pondId){
        WeightRandom<Items> weightRandom = weightItemMap.get(pondId);
        return weightRandom.next();
    }


}
