package com.hm.model.strength;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreInfo {
    private String uid;
    // 旋转角度
    private int angle;
    // 前端使用
    private List<Double> curPos = Lists.newArrayList();
    private List<Integer> gridList = Lists.newArrayList();

    public StoreInfo(StrengthStore store, List<Integer> gridList, JsonMsg msg){
        this.uid = store.getUid();
        this.angle = msg.getInt("angle");
        this.curPos = StringUtil.splitStr2DoubleList(msg.getString("curPos"), ",");
        this.gridList = gridList;
    }

    public boolean checkGrid(List<Integer> gridList){
        return CollUtil.containsAny(this.gridList, gridList);
    }
}
