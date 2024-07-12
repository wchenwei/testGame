package com.hmkf.level.king;

import java.util.List;

import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hmkf.level.GameTypeGroup;

public class KingGroupInfo {
    public GameTypeGroup gameTypeGroup;
    public List<KingInfo> kings = Lists.newArrayList();
    private long updateTime;

    public KingGroupInfo(GameTypeGroup gameTypeGroup) {
        this.gameTypeGroup = gameTypeGroup;
        reloadKing();
    }

    public void reloadKing() {
//        List<KingInfo> kings = Lists.newArrayList();
//        for (int id : gameTypeGroup.getKings()) {
//            kings.add(new KingInfo(id));
//        }
//        this.kings = kings;
//        this.updateTime = System.currentTimeMillis();
    }

    public List<KingInfo> getKings() {
        if (System.currentTimeMillis() - this.updateTime > 5 * GameConstants.MINUTE) {
            this.kings.forEach(e -> e.reloadInfo());
            this.updateTime = System.currentTimeMillis();
        }
        return this.kings;
    }


}
