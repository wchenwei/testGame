package com.hm.action.kf.kfworldwar.sum;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.rediscenter.CenterRedisUtils;
import com.hm.server.GameServerManager;
import com.hm.util.ServerUtils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 创建服务器->归属的teamId
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/5/27 15:11
 */
public class KfWorldWarServerTeamUtils {
    public static Map<Integer, Integer> server2TeamIdMap = Maps.newConcurrentMap();

    public static void reloadTeamMap() {
        server2TeamIdMap = Maps.newHashMap(getAllServerTeamFromDB());
    }

    public static int getServerTeamID(int serverId) {
        if (server2TeamIdMap.containsKey(serverId)) {
            return server2TeamIdMap.get(serverId);
        } else {
            int teamId = getTeamIdFromDB(serverId);
            if (teamId <= 0) {
                return 0;
            }
            server2TeamIdMap.put(serverId, teamId);
            return teamId;
        }
    }

    /**
     * 获取此物理机上运行的所有teamid列表
     *
     * @return
     */
    public static Set<Integer> getTeamIdListByMachine() {
        return server2TeamIdMap.values().stream().filter(e -> GameServerManager.getInstance().containServer(e))
                .collect(Collectors.toSet());
    }

    public static int getPlayerTeamID(long playerId) {
        return getServerTeamID(ServerUtils.getCreateServerId(playerId));
    }

    /**
     * 加入没有计算合并服务器的服务器队伍信息
     *
     * @param teamMap
     */
    public static void addServerTeamNoMerge(Set<Integer> serverIds, int teamId) {
        if (serverIds.isEmpty()) {
            return;
        }
        Map<Integer, Integer> teamMap = Maps.newHashMap();
        for (Integer serverId : serverIds) {
            teamMap.put(serverId, teamId);
        }
        addServerTeam(teamMap);
    }

    public static void addServerTeam(Map<Integer, Integer> teamMap) {
        Map<String, String> dataMap = teamMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey() + "", e -> e.getValue() + ""));
        if (dataMap.isEmpty()) {
            return;
        }
        try (Jedis jedis = getJedis()) {
            jedis.hmset(buildKey(), dataMap);
        }
    }

    public static Map<Integer, Integer> getAllServerTeamFromDB() {
        try (Jedis jedis = getJedis()) {
            Map<String, String> dataMap = jedis.hgetAll(buildKey());
            return dataMap.entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), e -> Integer.parseInt(e.getValue())));
        }
    }

    public static void delRedisTeam() {
        try (Jedis jedis = getJedis()) {
            jedis.del(buildKey());
        }
        reloadTeamMap();
    }

    public static int getTeamIdFromDB(int serverId) {
        try (Jedis jedis = getJedis()) {
            String data = jedis.hget(buildKey(), serverId + "");
            if (StrUtil.isEmpty(data)) {
                return 0;
            }
            return Integer.parseInt(data);
        }
    }


    public static String buildKey() {
        return MongoUtils.getGameDBName() + "_WorldWar2TeamId" + KfWorldWarSumUtils.getSnumId();
    }

    public static Jedis getJedis() {
        return CenterRedisUtils.getWorldCityPool().getResource();
    }

    public static ArrayListMultimap<Integer, Integer> buildServerDbIds(List<ServerInfo> serverInfoList) {
        ArrayListMultimap<Integer, Integer> serverGroups = ArrayListMultimap.create();
        for (ServerInfo serverInfo : serverInfoList) {
            int dbId = serverInfo.getDb_id() > 0 ? serverInfo.getDb_id() : serverInfo.getServer_id();
            serverGroups.put(dbId, serverInfo.getServer_id());
        }
        return serverGroups;
    }
}
