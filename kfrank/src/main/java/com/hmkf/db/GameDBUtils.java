package com.hmkf.db;

import java.util.List;
import java.util.stream.Collectors;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.server.GameServerType;
import com.hmkf.gametype.GameTypeUtils;
import com.hmkf.server.ServerGroupManager;

public class GameDBUtils {
    public static void initGameDB() {

        for (GameServerType myType : GameTypeUtils.getAllServerTypes()) {
            int typeId = myType.getId();
            List<Integer> idsList = ServerGroupManager.getServerList()
                    .stream().filter(e -> e.getServer_typeid() == typeId)
                    .filter(e -> e.getDb_id() == 0 || e.getDb_id() == e.getServer_id())
                    .filter(e -> e.isOpen())
                    .map(e -> e.getServer_id()).collect(Collectors.toList());
            for (int serverId : idsList) {
                MongoUtils.getGameMongodDB(serverId);
                System.err.println("加载游戏数据:" + serverId);
            }
        }
    }
}	
