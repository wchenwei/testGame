package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.model.tank.EvoGiftInfo;
import com.hm.model.tank.Tank;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName PlayerTankEvoGift
 * @Deacription 坦克超进化礼包
 *  改为 全服的 超进化礼包 逻辑 2022.4.7
 * @Author zxj
 * @Date 2021/9/13 10:39
 * @Version 1.0
 **/
@Deprecated
public class PlayerTankEvoGift extends PlayerDataContext {
    //已经弹出过礼包的tank列表<tankid, evoinfo>
    private ConcurrentHashMap<Integer, EvoGiftInfo> tankMap = new ConcurrentHashMap<Integer, EvoGiftInfo>();
    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerTankEvoGift", this);
    }

    public int getTimes() {
        return tankMap.size();
    }

    public List<Integer> getTankIds() {
        return tankMap.keySet().stream().filter(e->{return tankMap.get(e).getEnd_time()>System.currentTimeMillis();}).collect(Collectors.toList());
    }

    public EvoGiftInfo getEvoGift(int tankId) {
        return tankMap.get(tankId);
    }

    public int getTankShowTimes(int tankId) {
        if(!tankMap.containsKey(tankId)) {
            return 0;
        }
        return tankMap.get(tankId).getTimes();
    }

    public void addTankEvoGift(Tank tank, int lastTime){
        EvoGiftInfo tempInfo = tankMap.get(tank.getId());
        if(null==tempInfo) {
            tempInfo =new EvoGiftInfo(lastTime);
        }else {
            tempInfo.initEndTime(lastTime);
        }
        tempInfo.addTimes();
        tankMap.put(tank.getId(), tempInfo);
        SetChanged();
    }

    public void addRecharge(int rechargeId, int tankId) {
        EvoGiftInfo tempInfo = tankMap.get(tankId);
        if(null==tempInfo) {
            return;
        }
        tempInfo.addBuyTimes(rechargeId);
        SetChanged();
    }
}
