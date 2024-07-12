package com.hmkf.db;

import com.hm.db.KFPlayerFiledBuilder;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hmkf.server.ServerGroupManager;

public class KfPlayerUtils {
    public static Player getPlayer(long playerId) {
        return KFPlayerCacheManager.getInstance().getPlayer(playerId);
    }

    public static Player getPlayerFromDB(long playerId) {
        return KFPlayerCacheManager.getInstance().getPlayer(playerId);
    }

    public static Player getPlayerFromDB2(long playerId) {
        int dbId = ServerGroupManager.getIntance().getServerDbId(playerId);
        MongodDB mongo = MongoUtils.getGameMongodDB(dbId);
        return KFPlayerFiledBuilder.getPlayerFromKF(mongo, playerId);
    }

    public static Guild getGuildFromDB(Player player) {
        int dbId = ServerGroupManager.getIntance().getServerDbId(player.getId());
        return MongoUtils.getGameMongodDB(dbId).get(player.getGuildId(), Guild.class);
    }

    public static int getGuildId(long playerId) {
        PlayerRedisData temp = RedisUtil.getPlayerRedisData(playerId);
        return temp == null ? 0 : temp.getGuildId();
    }
}
