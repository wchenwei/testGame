package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.GachaElseTemplate;
import com.hm.config.excel.templaextra.GachaConfigTemplateImpl;
import com.hm.config.excel.templaextra.GachaRewardTemplateImpl;
import com.hm.enums.GachaRewardType;
import com.hm.enums.GachaType;
import com.hm.model.item.Items;
import com.hm.model.item.WeightItem;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import com.google.common.collect.*;
@Config
public class GachaConfig extends ExcleConfig {
    private ListMultimap<GachaRewardType, WeightItem> rewardMap = ArrayListMultimap.create();
    private Map<GachaType, GachaConfigTemplateImpl> configMap = Maps.newConcurrentMap();
    // max_level:reward
    private Map<Integer, Integer> elseMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
        loadRewardConfig();
        loadConfigTemplate();
        loadElseConfig();
    }

    private void loadRewardConfig() {
        List<GachaRewardTemplateImpl> list = JSONUtil.fromJson(getJson(GachaRewardTemplateImpl.class), new TypeReference<List<GachaRewardTemplateImpl>>() {
        });

        ListMultimap<GachaRewardType, WeightItem> tempMap = ArrayListMultimap.create();
        for (GachaRewardTemplateImpl template : list) {
            template.init();
            if (template.getNormalItems() != null) {
                tempMap.put(GachaRewardType.Normal, template.getNormalItems());
            }
            if (template.getOnceNotGetItems() != null) {
                tempMap.put(GachaRewardType.OnceNotGet, template.getOnceNotGetItems());
            }
            if (template.getOnceGetTankItems() != null) {
                tempMap.put(GachaRewardType.OnceGetTank, template.getOnceGetTankItems());
            }
            if (template.getTenNotGetItems() != null) {
                tempMap.put(GachaRewardType.TenNotGet, template.getTenNotGetItems());
            }
            if (template.getTenGetTankItems() != null) {
                tempMap.put(GachaRewardType.TenGetTank, template.getTenGetTankItems());
            }
            if (template.getRateTankItems() != null) {
                tempMap.put(GachaRewardType.TankRate, template.getRateTankItems());
            }
            if (template.getsRateTankItems() != null) {
                tempMap.put(GachaRewardType.STankRate, template.getsRateTankItems());
            }
        }
        this.rewardMap = ImmutableListMultimap.copyOf(tempMap);
    }

    private void loadConfigTemplate() {
        Map<GachaType, GachaConfigTemplateImpl> map = Maps.newConcurrentMap();
        List<GachaConfigTemplateImpl> list = JSONUtil.fromJson(getJson(GachaConfigTemplateImpl.class), new TypeReference<List<GachaConfigTemplateImpl>>() {
        });

        for (GachaConfigTemplateImpl template : list) {
            template.init();
            map.put(GachaType.num2enum(template.getType()), template);
        }

        configMap = ImmutableMap.copyOf(map);
    }

    private void loadElseConfig() {
        List<GachaElseTemplate> list = JSONUtil.fromJson(getJson(GachaElseTemplate.class), new TypeReference<List<GachaElseTemplate>>() {
        });
        Map<Integer, Integer> map = list.stream().collect(Collectors.toMap(GachaElseTemplate::getMax_level, GachaElseTemplate::getReward));
        elseMap = ImmutableMap.copyOf(map);
    }

    public GachaConfigTemplateImpl getGachaConfigTemplateImpl(GachaType gachaType) {
        if (configMap.containsKey(gachaType)) {
            return configMap.get(gachaType);
        }
        return null;
    }

    public Map<Items, Integer> getRewardMap(GachaRewardType rewardType) {
        Map<Items, Integer> map = Maps.newHashMap();
        if (!rewardMap.containsKey(rewardType)) {
            return map;
        }

        List<WeightItem> weightItems = rewardMap.get(rewardType);
        map = weightItems.stream().collect(Collectors.toMap(WeightItem::getItems, WeightItem::getWeight, (a, b) -> b));
        return map;
    }

    /**
     * 根据用户等级获取掉落id
     *
     * @param lv player level
     * @return
     */
    public int getElseReward(int lv) {
        TreeSet<Integer> treeSet = new TreeSet<>(elseMap.keySet());
        return treeSet.stream().filter(level -> level >= lv).findFirst().map(level -> elseMap.get(level)).orElse(0);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(GachaRewardTemplateImpl.class, GachaConfigTemplateImpl.class, GachaElseTemplate.class);
    }
}
