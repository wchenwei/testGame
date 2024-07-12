package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.model.tank.ControlLab;

import java.util.Map;

public class PlayerElement extends PlayerBagBase{
    private Map<Integer, ControlLab> labs = Maps.newConcurrentMap();

    @Override
    public void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerElement", this);
    }

    public void addLib(ControlLab controlLab) {
        this.labs.put(controlLab.getId(),controlLab);
        SetChanged();
    }

    public ControlLab getLab(int id) {
        return this.labs.get(id);
    }

    public boolean reduce(int id,int count) {
        ControlLab lab = labs.get(id);
        if(lab==null){
            return false;
        }
        lab.reduce(count);
        SetChanged();
        return true;
    }

    public void clearLab() {
        this.labs.clear();
        SetChanged();
    }

    public ControlLab getNowLab() {
        return labs.values().stream().filter(t -> t.getNum() > 0).findFirst().orElse(null);
    }
}
