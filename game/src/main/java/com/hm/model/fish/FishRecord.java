package com.hm.model.fish;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @introduce:
 * @author: wyp
 * @DATE: 2023/10/31
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FishRecord {
    private int id;
    // 尺寸大小
    private int min;
    private int max;
    // 是否领取过奖励
    private boolean reward;
    // 获取时间
    private long time;

    public FishRecord(int id){
        this.id = id;
        this.time = System.currentTimeMillis();
    }

    public void updateSize(FishVO fishVO){
        int size = fishVO.getSize();
        if(size > max || size < min){
            fishVO.setUpdateSize(true);
        }
        this.min = this.min<= 0 ? size : Math.min(min, size);
        this.max = Math.max(max, size);
    }
}
