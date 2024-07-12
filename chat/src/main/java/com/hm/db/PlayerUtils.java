package com.hm.db;


import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.model.Player;
import com.hm.redis.GuildRedisData;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.RedisUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlayerUtils {
    public static Player getPlayer(long playerId) {
        Player player = new Player(playerId);
        PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(playerId);
        if (playerRedisData == null) return null;
        player.setPlayerInfo(playerRedisData);
        if (player.getGuildId() > 0) {
            GuildRedisData guildRedisData = RedisUtil.getGuildRedisData(player.getGuildId());
            if (guildRedisData != null) {
                player.setGuildInfo(guildRedisData);
            }
        }
        return player;
    }


    //根据ids，查询出多个用户信息
    public static Map<Long, Player> getPlayerByIds(Integer[] ids) {
        MongodDB mongo = ChatMongoUtils.getChatMongoDB();
        Query query = new Query();
        Criteria c = new Criteria();
        c.and("id").in(Arrays.asList(ids));
        query.addCriteria(c);
        List<Player> listPlayer = mongo.query(query, Player.class);
        return PlayerUtils.getMapPlayer(listPlayer);
    }

    //list转map
    private static Map<Long, Player> getMapPlayer(List<Player> listPlayer) {
        Map<Long, Player> map = Maps.newHashMap();
        for (Player player : listPlayer) {
            map.put(player.getId(), player);
        }
        return map;
    }
}










