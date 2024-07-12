package com.hmkf.db;

import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hmkf.container.KFPlayerContainer;
import com.hmkf.model.KFBasePlayer;
import com.hmkf.model.KFPlayer;

public class KfDBUtils {
    public static final String KFSportsName = "kf_ranking" + ServerConfig.getInstance().getKFDBName();

    public static <T> T getPlayerFromDB(long playerId, Class<T> entityClass) {
        MongodDB mongo = getMongoDB();
        if (mongo == null) {
            return null;
        }
        return mongo.get(playerId, entityClass);
    }

    public static KFPlayer getPlayerSports(long playerId) {
        KFPlayerContainer playerContainer = SpringUtil.getBean(KFPlayerContainer.class);
        KFPlayer player = playerContainer.getPlayer(playerId);
        if (player != null) {
            return player;
        }
        return KFPlayerDBCache.getInstance().getPlayer(playerId);
    }


    public static void saveOrUpdate(KFBasePlayer player) {
        getMongoDB().update(player);
    }

    public static MongodDB getMongoDB() {
        return MongoUtils.getMongodDB(KFSportsName);
    }

}
