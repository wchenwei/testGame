package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveRewardQqdatingTemplateImpl;
import com.hm.config.excel.templaextra.ActiveRewardQqvipTemplateImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 * @date 2021/8/4 17:59
 */
@Config
public class BlueVipConfig extends ExcleConfig {
    private Map<Integer, ActiveRewardQqvipTemplateImpl> map = Maps.newConcurrentMap();
    private Map<Integer, ActiveRewardQqdatingTemplateImpl> QQPrivilege = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
//        laodQQVip();
//        laodPrivilege();
    }

    private void laodPrivilege() {
        List<ActiveRewardQqdatingTemplateImpl> tempList = JSONUtil.fromJson(getJson(ActiveRewardQqdatingTemplateImpl.class), new TypeReference<ArrayList<ActiveRewardQqdatingTemplateImpl>>() {
        });
        for (ActiveRewardQqdatingTemplateImpl template : tempList) {
            template.init();
        }
        Map<Integer, ActiveRewardQqdatingTemplateImpl> collect = tempList.stream().collect(Collectors.toMap(ActiveRewardQqdatingTemplateImpl::getId, Function.identity()));
        this.QQPrivilege = ImmutableMap.copyOf(collect);
    }

    private void laodQQVip() {
        List<ActiveRewardQqvipTemplateImpl> tempList = JSONUtil.fromJson(getJson(ActiveRewardQqvipTemplateImpl.class), new TypeReference<ArrayList<ActiveRewardQqvipTemplateImpl>>() {
        });
        for (ActiveRewardQqvipTemplateImpl template : tempList) {
            template.init();
        }
        Map<Integer, ActiveRewardQqvipTemplateImpl> collect = tempList.stream().collect(Collectors.toMap(ActiveRewardQqvipTemplateImpl::getId, Function.identity()));
        this.map = ImmutableMap.copyOf(collect);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveRewardQqvipTemplateImpl.class, ActiveRewardQqdatingTemplateImpl.class);
    }

    public ActiveRewardQqvipTemplateImpl getById(int id) {
        return map.getOrDefault(id, null);
    }

    public ActiveRewardQqdatingTemplateImpl getQQPrivilegeById(int id) {
        return QQPrivilege.getOrDefault(id, null);
    }

}
