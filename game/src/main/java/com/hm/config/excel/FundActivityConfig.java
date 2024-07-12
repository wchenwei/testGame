package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveLoginProjectTemplateImpl;
import com.hm.config.excel.templaextra.ActiveProjectGroupbuyTemplateImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-06-11
 *
 * @author Administrator
 */
@Config
public class FundActivityConfig extends ExcleConfig {
    /**
     * 登陆天数:object
     */
    private Map<Integer, ActiveLoginProjectTemplateImpl> loginMap = Maps.newConcurrentMap();
    /**
     * 档位:object
     */
    private Map<Integer, ActiveProjectGroupbuyTemplateImpl> groupMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
        Map<Integer, ActiveLoginProjectTemplateImpl> tempLoginMap = Maps.newConcurrentMap();
        List<ActiveLoginProjectTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveLoginProjectTemplateImpl.class), new TypeReference<List<ActiveLoginProjectTemplateImpl>>() {
        });
        list.forEach(template -> {
            template.init();
            tempLoginMap.put(template.getDay(), template);
        });
        loginMap = ImmutableMap.copyOf(tempLoginMap);

        Map<Integer, ActiveProjectGroupbuyTemplateImpl> tempGroupMap = Maps.newConcurrentMap();
        List<ActiveProjectGroupbuyTemplateImpl> list1 = JSONUtil.fromJson(getJson(ActiveProjectGroupbuyTemplateImpl.class), new TypeReference<List<ActiveProjectGroupbuyTemplateImpl>>() {
        });
        list1.forEach(template -> {
            template.init();
            tempGroupMap.put(template.getId(), template);
        });
        groupMap = ImmutableMap.copyOf(tempGroupMap);
    }

    public int loginRewardDayCount() {
        return loginMap.size();
    }

    public ActiveLoginProjectTemplateImpl getLoginConfig(int day) {
        return loginMap.getOrDefault(day, null);
    }

    public ActiveProjectGroupbuyTemplateImpl getGrouponConfig(int point) {
        return groupMap.values().stream().filter(v -> v.getNeed_point() <= point).
                max(Comparator.comparing(ActiveProjectGroupbuyTemplateImpl::getNeed_point)).orElse(null);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveLoginProjectTemplateImpl.class, ActiveProjectGroupbuyTemplateImpl.class);
    }
}
