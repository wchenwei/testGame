package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveWarmUpBoxTemplateImpl;
import com.hm.config.excel.templaextra.ActiveWarmUpGiftTemplateImpl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 * @date 2020/9/14 9:26
 */
@Config
public class ActiveWarmUpGiftConfig extends ExcleConfig {

    //计费点id - ID
    private Map<Integer,Integer> rechargeMap = Maps.newHashMap();

    private Map<Integer, ActiveWarmUpGiftTemplateImpl> map = Maps.newHashMap();

    private Map<Integer, ActiveWarmUpBoxTemplateImpl> boxMap = Maps.newHashMap();


    @Override
    public void loadConfig() {
//        loadActiveWarmUpGiftTemplate();
//        loadActiveWarmUpBoxTemplate();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveWarmUpGiftTemplateImpl.class,
                ActiveWarmUpBoxTemplateImpl.class
        );
    }

    private void loadActiveWarmUpGiftTemplate() {
        List<ActiveWarmUpGiftTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveWarmUpGiftTemplateImpl.class), new TypeReference<List<ActiveWarmUpGiftTemplateImpl>>() {});
        list.forEach(e -> {
            e.init();
        });
        Map<Integer, ActiveWarmUpGiftTemplateImpl> collect = list.stream().collect(Collectors.toMap(ActiveWarmUpGiftTemplateImpl::getId, Function.identity()));
        Map<Integer, Integer> collect1 = list.stream().filter(e->e.getRecharge_gift_id()>0).collect(Collectors.toMap(ActiveWarmUpGiftTemplateImpl::getRecharge_gift_id, ActiveWarmUpGiftTemplateImpl::getId));

        rechargeMap = ImmutableMap.copyOf(collect1);
        map = ImmutableMap.copyOf(collect);
    }

    private void loadActiveWarmUpBoxTemplate() {
        List<ActiveWarmUpBoxTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveWarmUpBoxTemplateImpl.class), new TypeReference<List<ActiveWarmUpBoxTemplateImpl>>() {});
        list.forEach(e -> {
            e.init();
        });
        Map<Integer, ActiveWarmUpBoxTemplateImpl> collect = list.stream().collect(Collectors.toMap(ActiveWarmUpBoxTemplateImpl::getId, Function.identity()));
        boxMap = ImmutableMap.copyOf(collect);
    }

    public ActiveWarmUpGiftTemplateImpl getActiveWarmUpGiftTemplateImpl(int id){
        return map.getOrDefault(id,null);
    }

    public ActiveWarmUpBoxTemplateImpl getActiveWarmUpBoxTemplateImpl(int id){
        return boxMap.getOrDefault(id,null);
    }

    public int getIntegrate(int id){
        ActiveWarmUpGiftTemplateImpl warmUpGiftTemplate = map.getOrDefault(id, null);
        return warmUpGiftTemplate == null?0:warmUpGiftTemplate.getScorce();
    }

    public boolean containRechargeId(int rechargeId) {
        return rechargeMap.containsKey(rechargeId);
    }

    public int getIdByRechargeId(int rechargeId) {
        return rechargeMap.getOrDefault(rechargeId,0);
    }
}
