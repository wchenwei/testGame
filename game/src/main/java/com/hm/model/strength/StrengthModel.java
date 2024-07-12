package com.hm.model.strength;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrengthModel {
    // TankType
    private int type;
    // 镶嵌的宝石id -- 占用的格子
    private Map<String, StoreInfo> storeMap = Maps.newConcurrentMap();

    public StrengthModel(int type){
        this.type = type;
    }

    public List<String> getAllStore(){
        return Lists.newArrayList(storeMap.keySet());
    }

    public boolean useStore(String uid){
        return CollUtil.contains(storeMap.keySet(), uid);
    }

    public void down(String uid){
        this.storeMap.remove(uid);
    }

    public void up(String uid, StoreInfo storeInfo){
        this.storeMap.put(uid, storeInfo);
    }

    // 获取所有已 上阵的格子位置
    public List<Integer> getGridList(){
        return storeMap.values().stream()
                .flatMap(storeInfo -> storeInfo.getGridList().stream())
                .collect(Collectors.toList());
    }

    /**
     *  检查 格子位置是否与 其他配件位置重合
     * @param store 上阵的配件
     * @param gridList 上阵的格子位置
     * @return
     */
    public boolean checkBlankGrid(StrengthStore store, List<Integer> gridList){
        return storeMap.entrySet().stream()
                .filter(entry -> !StrUtil.equals(store.getUid(), entry.getKey()))
                .anyMatch(entry-> entry.getValue().checkGrid(gridList));
    }
}
