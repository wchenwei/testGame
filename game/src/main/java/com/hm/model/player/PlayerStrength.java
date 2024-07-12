package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.model.strength.StrengthModel;
import com.hm.model.strength.StrengthStore;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerStrength extends PlayerDataContext {
    // 力量机器类型 -- 机器
    private Map<Integer, StrengthModel> map = Maps.newHashMap();
    // 拥有的 机器零件
    private Map<String, StrengthStore> storeMap = Maps.newHashMap();
    // 幸运值
    @Setter
    private int luckyValue;

    public StrengthModel getStrengthModel(int type){
        StrengthModel strengthModel = map.get(type);
        if(strengthModel == null){
            strengthModel = new StrengthModel(type);
            map.put(type, strengthModel);
        }
        return strengthModel;
    }

    public StrengthStore getStore(String uid){
        return storeMap.get(uid);
    }

    public List<StrengthModel> getAllStrengthModel(){
        return Lists.newArrayList(map.values());
    }

    public void addStore(StrengthStore store){
        storeMap.put(store.getUid(), store);
    }

    // 获取未锁定 且没上阵的配件
    public List<StrengthStore> getFreeStoreList(){
        return storeMap.values().stream().filter(store -> {
            if(store.isLock()){
                return false;
            }
            return !checkStoreUsed(store);
        }).collect(Collectors.toList());
    }

    public boolean checkStoreUsed(StrengthStore store){
        if(store == null){
            return false;
        }
        return map.values().stream().anyMatch(e-> e.useStore(store.getUid()));
    }

    public StrengthModel getModelByStore(String storeUid){
        return map.values().stream().filter(e-> e.useStore(storeUid)).findFirst().orElse(null);
    }

    public void removeStore(StrengthStore store){
        storeMap.remove(store.getUid());
    }

    public void removeStore(List<StrengthStore> storeList){
        storeList.forEach(this::removeStore);
        SetChanged();
    }

    public boolean isLucky() {
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        boolean luck = luckyValue >= commValueConfig.getCommValue(CommonValueType.Strength_Lucky_Limit);
        if(luck){
            this.luckyValue = 0;
        }
        return luck;
    }

    public void incLuckyValue(int inc) {
        luckyValue += inc;
    }

    public boolean checkMaxNum(int num){
        return this.storeMap.size() + num <= GameConstants.Strength_Store_Limit;
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerStrength", this);
    }
}
