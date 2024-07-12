
package com.hm.container;

import com.google.common.collect.Maps;
import com.hm.db.PlayerUtils;
import com.hm.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Title: GamePlayerContainer.java
 * Description:游戏玩家容器
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 *
 * @author 李飞
 * @version 1.0
 * @date 2015年4月22日 上午10:52:23
 */
@Service
public class PlayerContainer {
    //所有在线玩家
    private Map<Long, Player> playerMap = Maps.newConcurrentMap();

    public void addPlayer2Map(Player player) {
        this.playerMap.put(player.getId(), player);
    }

    public void removePlayer(long playerId) {
        this.playerMap.remove(playerId);
    }

    public Player getPlayer(long playerId) {
        Player player = this.playerMap.get(playerId);
        if (player == null) {
            player = PlayerUtils.getPlayer(playerId);
            if (player != null) {
                addPlayer2Map(player);
            }
        }
        return player;
    }

    public Player getOnlinePlayer(long playerId) {
        return playerMap.get(playerId);
    }

}

