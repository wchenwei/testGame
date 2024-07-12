package com.hm.redis.trade;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.troop.client.ClientTroop;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.redis.mode.AbstractRedisHashMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家航运信息
 * @date 2023/8/28 13:44
 */
@Setter
@Getter
@NoArgsConstructor
public class PlayerStock extends AbstractRedisHashMode {
    private long playerId;
    private long ownerId;//大股东是谁
    private List<Long> subList = Lists.newArrayList();//我的所有子股东
    private long val;//上缴数量
    private String defTroops;
    private long combat;


    public PlayerStock(long playerId) {
        this.playerId = playerId;
    }

    public void changeOwnerId(long newOwnerId) {
        this.ownerId = newOwnerId;
        this.val = 0;
        saveDB();
    }

    public void addVal(long add) {
        this.val += add;
        saveDB();
    }

    public void removeSubList(Long id) {
        if(this.subList.remove(id)) {
            saveDB();
        }
    }


    public void addSubList(Long id) {
        if(this.subList.add(id)) {
            saveDB();
        }
    }

    public void setDefTroops(String defTroops,long combat) {
        this.defTroops = defTroops;
        this.combat = combat;
        saveDB();
    }

    public List<Tank> getDefTankList(Player player) {
        ClientTroop clientTroop = ClientTroop.build(defTroops);
        return clientTroop.getArmyList().stream()
                .map(e -> player.playerTank().getTank(e.getId())).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public boolean isSubPlayer(int subId) {
        return CollUtil.contains(this.subList,subId);
    }

    @Override
    public String buildFiledKey() {
        return playerId+"";
    }

    @Override
    public String buildHashKey() {
        return "PlayerStockRedis";
    }

    public static PlayerStock getPlayerStock(long playerId) {
        PlayerStock playerStock = getPlayerStockOrNull(playerId);
        if(playerStock == null) {
            playerStock = new PlayerStock(playerId);
        }
        return playerStock;
    }
    public static PlayerStock getPlayerStockOrNull(long playerId) {
        return PlayerStock.getHashVal("PlayerStockRedis",playerId+"",PlayerStock.class);
    }

    public static Map<Long,PlayerStock> getPlayerStockForMap(List<Long> playerIds) {
        return PlayerStock.getHashValForKeyList("PlayerStockRedis",playerIds,PlayerStock.class)
                .stream().collect(Collectors.toMap(PlayerStock::getPlayerId,e -> e));
    }
}
