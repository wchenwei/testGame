package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;

import java.util.List;
import java.util.Map;

public class PlayerTankMaster extends PlayerDataContext{
    private int totalScore;// 总积分
    // 已领的tank和对应的星级
    private Map<Integer, Integer> tankStarMap = Maps.newHashMap();
    // 已领取的奖励
    private List<Integer> rewardIds = Lists.newArrayList();

    public int getTankStar(int tankId){
        return this.tankStarMap.getOrDefault(tankId, 0);
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTotalStar(){
        return tankStarMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void addTankStar(int tankId){
        this.tankStarMap.put(tankId, getTankStar(tankId)+1);
        SetChanged();
    }

    public void addScore(int score){
        this.totalScore += score;
        SetChanged();
    }

    public boolean isReward(int id){
        return rewardIds.contains(id);
    }

    public void addRewards(int id){
        this.rewardIds.add(id);
        SetChanged();
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerTankMaster", this);
    }
}
