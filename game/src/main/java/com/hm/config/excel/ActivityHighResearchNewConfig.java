package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveGachaAdvanceProgressTemplateImpl;
import com.hm.config.excel.templaextra.ActiveGachaAdvanceTemplateImpl;
import com.hm.enums.GachaRewardType;
import com.hm.model.item.Items;
import com.hm.model.item.WeightItem;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;
import com.google.common.collect.*;
@Config
public class ActivityHighResearchNewConfig extends ExcleConfig {
    private ListMultimap<GachaRewardType, WeightItem> rewardMap = ArrayListMultimap.create();
    /**
     * ActiveGachaAdvanceTemplate::getGround : WeightItem
     */
    private ListMultimap<Integer, ActiveGachaAdvanceTemplateImpl> groundMap = ArrayListMultimap.create();

    /**
     * ActiveGachaAdvanceProgressTemplateImpl::getId, ActiveGachaAdvanceProgressTemplateImpl
     */
    private Map<Integer, ActiveGachaAdvanceProgressTemplateImpl> progressMap = Maps.newConcurrentMap();


    public ActiveGachaAdvanceProgressTemplateImpl getProgressCfg(int id) {
        return progressMap.getOrDefault(id, null);
    }

    private void loadProgressConfig() {
        List<ActiveGachaAdvanceProgressTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveGachaAdvanceProgressTemplateImpl.class), new TypeReference<List<ActiveGachaAdvanceProgressTemplateImpl>>() {
        });

        Map<Integer, ActiveGachaAdvanceProgressTemplateImpl> m = Maps.newConcurrentMap();
        for (ActiveGachaAdvanceProgressTemplateImpl template : list) {
            template.init();
            m.put(template.getId(), template);
        }

        progressMap = ImmutableMap.copyOf(m);
    }

    private void loadRewardConfig() {
        /*List<ActiveGachaAdvanceTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveGachaAdvanceTemplateImpl.class), new TypeReference<List<ActiveGachaAdvanceTemplateImpl>>() {
        });

        ListMultimap<GachaRewardType, WeightItem> tempMap = ArrayListMultimap.create();
        for (ActiveGachaAdvanceTemplateImpl template : list) {
            template.init();
            // ground >= 0 的是活动期数对应的临时奖励
            // ground < 0 是通用奖励
            if (template.getGround() >= 0) {
                groundMap.put(template.getGround(), template);
                continue;
            }

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
            if (template.getTenGetPaper() != null) {
                tempMap.put(GachaRewardType.TenGetPaper, template.getTenGetPaper());
            }
        }
        this.rewardMap = ImmutableListMultimap.copyOf(tempMap);*/
    }

    /**
     * @param rewardType
     * @param ground     活动期数
     * @return
     */
    public Map<Items, Integer> getRewardMap(GachaRewardType rewardType, int ground) {
        // items : weight
        Map<Items, Integer> map = Maps.newHashMap();
        // 修正一下，添加活动期数对应的特殊奖励
        if (rewardType == GachaRewardType.TenGetTank) {
            for (ActiveGachaAdvanceTemplateImpl template : groundMap.get(ground)) {
                WeightItem weightItem = template.getTenGetTankItems();
                if (weightItem != null) {
                    map.put(weightItem.getItems(), weightItem.getWeight());
                }
            }
        }

        if (rewardType == GachaRewardType.TankRate) {
            for (ActiveGachaAdvanceTemplateImpl template : groundMap.get(ground)) {
                WeightItem weightItem = template.getRateTankItems();
                if (weightItem != null) {
                    map.put(weightItem.getItems(), weightItem.getWeight());
                }
            }
        }

        if (!rewardMap.containsKey(rewardType)) {
            return map;
        }

        // 普通奖励
        List<WeightItem> weightItems = rewardMap.get(rewardType);
        for (WeightItem weightItem : weightItems) {
            map.put(weightItem.getItems(), weightItem.getWeight());
        }

        return map;
    }

    /**
     * 额外送必得图纸
     *
     * @param ground
     * @return
     */
    public Items getExtraPaper(int ground) {
        // items : weight
        Map<Items, Integer> map = Maps.newHashMap();
        for (ActiveGachaAdvanceTemplateImpl template : groundMap.get(ground)) {
            WeightItem weightItem = template.getTenGetPaper();
            if (weightItem != null) {
                map.put(weightItem.getItems(), weightItem.getWeight());
            }
        }

        if (!map.isEmpty()) {
            return RandomUtils.buildWeightMeta(map).random();
        }
        return null;
    }

    @Override
    public void loadConfig() {
        loadRewardConfig();
        loadProgressConfig();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveGachaAdvanceProgressTemplateImpl.class, ActiveGachaAdvanceTemplateImpl.class);
    }
}
