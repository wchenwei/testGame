package com.hm.model.player;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description:贸易公司股东
 * @date 2023/8/28 11:27
 */
@Getter
public class PlayerTradeStock extends PlayerDataContext {
    private Map<Integer, List<Long>> typeMap = Maps.newHashMap();

    public List<Long> getRankList(int type) {
        return typeMap.get(type);
    }

    public void setRankList(int type,List<Long> rankList) {
        this.typeMap.put(type,rankList);
        SetChanged();
    }

    public void doDayReset() {
        if(!typeMap.isEmpty()){
            this.typeMap.clear();
            SetChanged();
        }
    }
}
