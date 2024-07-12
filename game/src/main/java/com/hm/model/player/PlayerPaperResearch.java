package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerPaperResearch extends PlayerDataContext{
    //坦克研究数据 key：tankId  value：研发次数
    private ConcurrentHashMap<Integer,Integer> tankResearchMap = new ConcurrentHashMap<>();

    public int getTankResearchCount(int tankId){
        return tankResearchMap.getOrDefault(tankId, 0);
    }

    //坦克研发
    public void tankResearch(int tankId) {
        tankResearchMap.put(tankId, getTankResearchCount(tankId) + 1);
        SetChanged();
    }

    public void resetDay(){
        tankResearchMap.clear();
        SetChanged();
    }


    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerPaperResearch", this);
    }
}
