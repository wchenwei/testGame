package com.hm.model.tank;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * @ClassName EvoGiftInfo
 * @Deacription TODO
 * @Author zxj
 * @Date 2021/9/14 11:02
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvoGiftInfo {
    long end_time;
    int times=0;
    HashMap<Integer, Integer> buyTimes = Maps.newHashMap();
    public EvoGiftInfo(int lastTime) {
        this.setEnd_time(System.currentTimeMillis()+lastTime*60*60*1000);
    }

    public void initEndTime(int lastTime) {
        if(this.getEnd_time()>System.currentTimeMillis()) {
            this.end_time=this.getEnd_time()+lastTime*60*60*1000;
        }else {
            this.end_time=System.currentTimeMillis()+lastTime*60*60*1000;
        }
    }

    public int getBuyTimes(int rechargeId) {
        return this.buyTimes.getOrDefault(rechargeId, 0);
    }

    public void addTimes() {
        this.times++;
    }

    public void addBuyTimes(int rechargeId) {
        buyTimes.put(rechargeId, buyTimes.getOrDefault(rechargeId, 0)+1);
    }
}
