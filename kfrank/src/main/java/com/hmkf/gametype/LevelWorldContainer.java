package com.hmkf.gametype;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.hmkf.level.LevelWorldGroup;

@Service
public class LevelWorldContainer {
    @Resource
    private KfGroupContainer groupContainer;

    private Map<Integer, LevelWorldGroup> worldMap = Maps.newConcurrentMap();

    public void loadData() {

    }

    public LevelWorldGroup getLevelWorldGroup(int gameType) {
        return this.worldMap.get(gameType);
    }

    public void clearData() {
        this.worldMap.clear();
    }
}
