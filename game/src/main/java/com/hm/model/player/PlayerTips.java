package com.hm.model.player;

import com.google.common.collect.Lists;

import java.util.List;

public class PlayerTips extends PlayerDataContext{

    private List<BaseTips> tipsList = Lists.newArrayList();

    public List<BaseTips> getTipsList() {
        return tipsList;
    }

    public void addTips(BaseTips tips){
        this.tipsList.add(tips);
        SetChanged();
    }

    public void clear(){
        this.tipsList.clear();
        SetChanged();
    }

}
