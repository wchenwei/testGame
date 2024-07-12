package com.hm.model.serverpublic;

import com.google.common.collect.Maps;
import com.hm.util.ServerUtils;

import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 服务器redis数据
 * @date 2023/12/12 15:14
 */
public class ServerRedisDataUtils {
    public static Map<Integer, ServerRedisData> serverMap = Maps.newConcurrentMap();


    public static ServerRedisData getServerRedisData(long playerId) {
        int serverId = ServerUtils.getCreateServerId(playerId);
        return getServerRedisDataByServerId(serverId);
    }

    public static ServerRedisData getServerRedisDataByServerId(int serverId) {
        ServerRedisData serverRedisData = serverMap.get(serverId);
        if(serverRedisData == null) {
            serverRedisData = ServerRedisData.get(serverId);
            if(serverRedisData == null) {
                System.out.println(serverId+" ServerRedisData出错找不到id");
                serverRedisData = new ServerRedisData(serverId);
            }
            serverMap.put(serverId,serverRedisData);
        }
        return serverRedisData;
    }

    public static void addServerData(int serverId,ServerRedisData serverRedisData) {
        serverMap.put(serverId,serverRedisData);
    }

    public static long getServerOpenTime(long playerId) {
        ServerRedisData serverRedisData = getServerRedisData(playerId);
        return serverRedisData.getOpenTime();
    }

}
