package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ActiveRecharge4dayLevelTemplate;
import com.hm.config.excel.templaextra.ActiveRecharge4dayNewTemplateImpl;
import com.hm.model.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-10
 */
@Config
public class ContinuousPurchaseConfig extends ExcleConfig {
    private Map<Integer, ActiveRecharge4dayNewTemplateImpl> map = Maps.newConcurrentMap();
    private Map<Integer, ActiveRecharge4dayLevelTemplate> lvMap = Maps.newConcurrentMap();

    public List<Items> getRewardList(int id) {
        if (map.containsKey(id)) {
            return map.get(id).getRewardList();
        }
        return Lists.newArrayList();
    }

    public int getId(int chargeGold, int playerLv, int index) {
        Optional<ActiveRecharge4dayNewTemplateImpl> any = map.values().stream().
                filter(t->t.getCharge_gold().equals(chargeGold)).
                filter(t -> t.getLv_down() <= playerLv && playerLv <= t.getLv_up()).
                filter(t -> t.getStage_index().equals(index)).findAny();
        return any.isPresent() ? any.get().getId() : -1;
    }

    public ActiveRecharge4dayNewTemplateImpl getCfg(int id) {
        return map.getOrDefault(id, null);
    }

    public int getChargeGoldNum(int id) {
        if (lvMap.containsKey(id)) {
            return lvMap.get(id).getCharge_gold();
        }
        return 0;
    }

    @Override
    public void loadConfig() {
        Map<Integer, ActiveRecharge4dayNewTemplateImpl> t = Maps.newConcurrentMap();
        List<ActiveRecharge4dayNewTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveRecharge4dayNewTemplateImpl.class), new TypeReference<ArrayList<ActiveRecharge4dayNewTemplateImpl>>() {
        });
        for (ActiveRecharge4dayNewTemplateImpl template : list) {
            template.init();
            t.put(template.getId(), template);
        }

        this.map = ImmutableMap.copyOf(t);

        List<ActiveRecharge4dayLevelTemplate> list1 = JSONUtil.fromJson(getJson(ActiveRecharge4dayLevelTemplate.class), new TypeReference<ArrayList<ActiveRecharge4dayLevelTemplate>>() {
        });
        Map<Integer, ActiveRecharge4dayLevelTemplate> t1 = list1.stream().collect(Collectors.toMap(ActiveRecharge4dayLevelTemplate::getId, Function.identity()));
        this.lvMap = ImmutableMap.copyOf(t1);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveRecharge4dayNewTemplateImpl.class, ActiveRecharge4dayLevelTemplate.class);
    }
}
