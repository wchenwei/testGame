package com.hm.action.troop.biz;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public class TempWayLine {
    private int startId;
    private int toId;
    private List<Integer> wayList = Lists.newArrayList();


    public TempWayLine(int startId, int toId, List<Integer> wayList) {
        this.startId = startId;
        this.toId = toId;
        this.wayList = wayList;
    }

    public int getWaySize() {
        return wayList.size();
    }
}
