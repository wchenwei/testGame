package com.hm.libcore.serverConfig;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.hm.libcore.mongodb.ServerInfo;

import java.util.concurrent.TimeUnit;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 服务器数据处理器
 * @date 2024/6/21 9:25
 */
public class ServerInfoCache {
    private static final ServerInfoCache instance = new ServerInfoCache();
    public static ServerInfoCache getInstance() {
        return instance;
    }

    private final LoadingCache<Integer, ServerInfo> cache;

    private ServerInfoCache() {
        cache = Caffeine.newBuilder()
                .maximumSize(4000)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<Integer, ServerInfo>() {
                    @Override
                    public ServerInfo load(Integer key) {
                        ServerInfo serverInfo = ServerInfo.getServerInfoFromDB(key);
                        if(serverInfo != null) {
                            GameServerMachine serverMachine = GameServerMachine.getServerMachine(serverInfo.getServermachineId());
                            serverInfo.setServerMachine(serverMachine);
                        }
                        return serverInfo;
                    }
                });
    }

    public ServerInfo getServerInfo(int serverId) {
        return cache.get(serverId);
    }


}
