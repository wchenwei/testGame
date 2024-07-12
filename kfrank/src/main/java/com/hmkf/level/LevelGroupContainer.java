package com.hmkf.level;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hmkf.gametype.KfGroupContainer;
import com.hmkf.model.KFBasePlayer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Biz
public class LevelGroupContainer {
    @Resource
    private KfGroupContainer groupContainer;

    private Map<Integer, LevelWorldGroup> worldMap = Maps.newConcurrentMap();

    public void loadData() {
        for (int gameType : groupContainer.getAllGameTypes()) {
            GameTypeGroup gameTypeGroup = GameTypeGroup.getGameTypeGroup(gameType);
            if(gameTypeGroup == null) {
                gameTypeGroup = loadNewGameType(gameType);
            }
//            List<RankGroup> groupList = Lists.newArrayList();
//            groupList.add(new RankGroup("kfrank8_1_1_20240426"));
//            groupList.add(new RankGroup("kfrank8_1_2_20240426"));
//            groupList.add(new RankGroup("kfrank8_1_3_20240426"));
//            groupList.add(new RankGroup("kfrank8_1_4_20240428"));
//            gameTypeGroup.getGroupMap().put(gameType,groupList);
//            gameTypeGroup.saveDB();

            gameTypeGroup.init();

            LevelWorldGroup worldGroup = new LevelWorldGroup(gameTypeGroup);
            this.worldMap.put(worldGroup.getGameTypeId(), worldGroup);
        }
    }

    public LevelWorldGroup getLevelWorldGroup(int gameTypeId) {
        return this.worldMap.get(gameTypeId);
    }

    public LevelWorldGroup getLevelWorldGroup(KFBasePlayer player) {
        return this.worldMap.get(player.getGameTypeId());
    }


    public List<LevelWorldGroup> getAllLevelWorldGroup() {
        return Lists.newArrayList(this.worldMap.values());
    }

    public void clearData() {
        this.worldMap.clear();
    }


    public GameTypeGroup loadNewGameType(int gameType) {
        log.error("=============加载游戏类组:" + gameType);
        GameTypeGroup gameTypeGroup = new GameTypeGroup();
        gameTypeGroup.setId(gameType);
        gameTypeGroup.saveDB();
        return gameTypeGroup;
    }
}
