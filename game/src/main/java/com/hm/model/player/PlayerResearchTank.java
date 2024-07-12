package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.config.GameConstants;
import com.hm.libcore.msg.JsonMsg;
import lombok.Getter;

import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2018-10-15
 */
@Getter
public class PlayerResearchTank extends PlayerDataContext {

    private int wishTankId;// 心愿tank
    private int seniorTimes; // 捕猎次数
    private int freeTimes; // 今日使用免费次数
    private long freeEndTime; // 免费冷却结束时间
    private transient Map<Integer, Integer> notPondTimesMap = Maps.newHashMap();// 累计不出的池子次数
    private transient Map<Integer, Integer> addWeightMap = Maps.newHashMap();// 累计不出的池子增加的权重

    public void resetSeniorTimes() {
        seniorTimes = 0;
        SetChanged();
    }

    public void incSeniorTimes() {
        seniorTimes++;
        SetChanged();
    }

    public boolean haveWish(){
        return wishTankId > 0;
    }

    public void resetWishTankId(int wishTankId) {
        this.wishTankId = wishTankId;
        SetChanged();
    }

    public int getNotPondWeight(int pondId){
        return addWeightMap.getOrDefault(pondId, 0);
    }

    public int getNotPondTimes(int pondId){
        return notPondTimesMap.getOrDefault(pondId, 0);
    }

    public void addNotPondWeight(int pondId, int addWeight){
        this.addWeightMap.put(pondId, getNotPondWeight(pondId)+addWeight);
        SetChanged();
    }

    public void addNotPondTimes(int pondId){
        this.notPondTimesMap.put(pondId, getNotPondTimes(pondId) + 1);
        SetChanged();
    }

    public void clearNotPondWeight(Integer pondId){
        this.notPondTimesMap.remove(pondId);
        this.addWeightMap.remove(pondId);
        SetChanged();
    }

    public void addFreeTimes(int cdSecond){
        this.freeTimes ++;
        this.freeEndTime = System.currentTimeMillis() + cdSecond * GameConstants.SECOND;
        SetChanged();
    }

    public void resetDay(){
        this.freeEndTime = 0;
        this.freeTimes = 0;
        SetChanged();
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerResearchTank", this);
    }
}
