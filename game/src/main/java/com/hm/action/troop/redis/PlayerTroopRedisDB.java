package com.hm.action.troop.redis;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.troop.TroopServerContainer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@RedisMapperType(type = MapperType.STRING_HASH)
public class PlayerTroopRedisDB extends BaseEntityMapper<Long> {
    private ArrayList<TroopRDB> troopList = Lists.newArrayList();
    private long combat;



    public static PlayerTroopRedisDB saveRedis(Player player) {
        List<WorldTroop> troopList = TroopServerContainer.of(player.getServerId()).getWorldTroopByPlayer(player);
        if(CollUtil.isEmpty(troopList)) {
            return null;//最少保留1只部队
        }
        PlayerTroopRedisDB troopRedisDB = new PlayerTroopRedisDB();
        troopRedisDB.setId(player.getId());
        long combat = 0;
        for (WorldTroop worldTroop : TroopServerContainer.of(player.getServerId()).getWorldTroopByPlayer(player)) {
            TroopRDB troopRDB = new TroopRDB(player,worldTroop);
            troopRedisDB.troopList.add(troopRDB);
            combat += troopRDB.getCombat();
        }
        troopRedisDB.combat = combat;
        troopRedisDB.saveDB();
        return troopRedisDB;
    }


    public static PlayerTroopRedisDB getPlayerTroopRedisDB(long playerId) {
        return queryOne(0,playerId,PlayerTroopRedisDB.class);
    }

}
