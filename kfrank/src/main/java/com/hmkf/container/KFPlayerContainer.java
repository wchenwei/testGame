
package com.hmkf.container;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.hmkf.action.level.KfLevelBiz;
import com.hm.message.KFRankMessageComm;
import com.hmkf.model.KFPlayer;

@Slf4j
@Service
public class KFPlayerContainer {
    @Resource
    private KfLevelBiz kfLevelBiz;

    //所有在线玩家
    private Map<Long, KFPlayer> gamePlayerMap = new ConcurrentHashMap<>();

    public void addPlayer2Map(KFPlayer player) {
        this.gamePlayerMap.put(player.getId(), player);
    }

    public void removePlayer(long id) {
        this.gamePlayerMap.remove(id);
    }

    public KFPlayer getPlayer(long id) {
        return this.gamePlayerMap.get(id);
    }

    public boolean isOnline(long id) {
        return this.gamePlayerMap.containsKey(id);
    }

    public List<KFPlayer> getOnlinePlayers() {
        return Lists.newArrayList(gamePlayerMap.values());
    }

    public void clearPlayer() {
        this.gamePlayerMap.clear();
    }

    public void doDayZero() {
        for (KFPlayer player : gamePlayerMap.values()) {
            try {
                kfLevelBiz.doDayZero(player);
                player.sendPlayerUpdate();

                player.sendMsg(KFRankMessageComm.S2C_ZeroMsg);
            } catch (Exception e) {
                log.error(player.getId() + "每次重置出错", e);
            }
        }
    }

}

