package com.hmkf.server;

import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.server.GameServerManager;
import com.hm.util.ServerUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerGroupManager {
    private static ServerGroupManager intance = new ServerGroupManager();

    public static ServerGroupManager getIntance() {
        return intance;
    }

    private Map<Integer, Integer> serverDbMap = Maps.newConcurrentMap();
    private Map<Integer, ServerInfo> serverMap = Maps.newConcurrentMap();

    public void init() {
        Map<Integer, GameServerMachine> machineMap = MongoUtils.getLoginMongodDB()
                .queryAll(GameServerMachine.class, "serverMachine")
                .stream().collect(Collectors.toMap(GameServerMachine::getId, e -> e));
        Map<Integer, ServerInfo> serverMap = Maps.newConcurrentMap();
        for (ServerInfo serverInfo : getServerList()) {
            serverInfo.setServerMachine(machineMap.get(serverInfo.getServermachineId()));
            serverMap.put(serverInfo.getServer_id(), serverInfo);
        }
        this.serverMap = serverMap;

        for (ServerInfo serverInfo : serverMap.values()) {
            //实际的合服id
            int dbId = serverInfo.getDb_id() > 0 ? serverInfo.getDb_id() : serverInfo.getServer_id();
            this.serverDbMap.put(serverInfo.getServer_id(), dbId);
        }
        GameServerManager.getInstance().putServerDbMap(this.serverDbMap);
    }

    public static List<ServerInfo> getServerList() {
        return MongoUtils.getLoginMongodDB().queryAll(ServerInfo.class, "serverInfo");
    }


    public int getServerDbId(long playerId) {
        int serverId = ServerUtils.getServerId(playerId);
        return this.serverDbMap.getOrDefault(serverId, serverId);
    }



    public ServerInfo getServerInfo(long playerId) {
        int serverId = ServerUtils.getServerId(playerId);
        return this.serverMap.get(serverId);
    }

    public ServerInfo getServerInfoByServerId(int serverId) {
        return this.serverMap.get(serverId);
    }



    public void reloadData() {
        this.serverDbMap.clear();
        this.serverMap.clear();
        init();
    }

    public static void main(String[] args) {
        ServerGroupManager.getIntance().init();
    }
}
