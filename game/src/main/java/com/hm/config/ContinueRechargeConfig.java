package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.ActiveRecharge4dayTemplateImpl;
import com.hm.model.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-10
 */
@Config
public class ContinueRechargeConfig extends ExcleConfig {
    private Map<Integer, ActiveRecharge4dayTemplateImpl> map = Maps.newConcurrentMap();

    public List<Items> getRewardList(int id) {
        if (map.containsKey(id)) {
            return map.get(id).getRewardList();
        }
        return Lists.newArrayList();
    }

    public int getId(int playerLv, int index) {
        Optional<ActiveRecharge4dayTemplateImpl> any = map.values().stream().
                filter(t -> t.getLv_down() <= playerLv && playerLv <= t.getLv_up()).
                filter(t -> t.getStage_index().equals(index)).findAny();
        return any.isPresent() ? any.get().getId() : -1;
    }

    public ActiveRecharge4dayTemplateImpl getCfg(int id) {
        return map.getOrDefault(id, null);
    }

    @Override
    public void loadConfig() {
        Map<Integer, ActiveRecharge4dayTemplateImpl> t = Maps.newConcurrentMap();
        List<ActiveRecharge4dayTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveRecharge4dayTemplateImpl.class), new TypeReference<ArrayList<ActiveRecharge4dayTemplateImpl>>() {
        });
        for (ActiveRecharge4dayTemplateImpl template : list) {
            template.init();
            t.put(template.getId(), template);
        }

        this.map = ImmutableMap.copyOf(t);

    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveRecharge4dayTemplateImpl.class);
    }
}
