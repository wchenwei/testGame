package com.hmkf.gametype;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.rpc.GameRpcClientUtils;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.util.GameIpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.util.ServerUtils;

@Slf4j
@Service
public class KfGroupContainer {

    private Map<Integer, Integer> groupMap = Maps.newConcurrentMap();
    private ArrayListMultimap<Integer, Integer> typeServerIdMap = ArrayListMultimap.create();

    public void init() {
        List<Integer> typeList = GameTypeUtils.getGameServerType();
        log.error("加载游戏类型:" + GSONUtils.ToJSONString(typeList));
        Map<Integer, Integer> groupMap = Maps.newConcurrentMap();
        List<ServerInfo> serverList = ServerUtils.getAllServerInfoFromDB()
                .stream().filter(e -> typeList.contains(e.getType())).collect(Collectors.toList());
        //注册rpc
        Map<Integer, GameServerMachine> machineMap = MongoUtils.getLoginMongodDB()
                .queryAll(GameServerMachine.class, "serverMachine")
                .stream().collect(Collectors.toMap(GameServerMachine::getId, e -> e));
        for (ServerInfo serverInfo : serverList) {
            GameServerMachine serverMachine = machineMap.get(serverInfo.getServermachineId());
            String rpcUrl = GameIpUtils.getGameRpcUrl(serverMachine);
            GameRpcClientUtils.changeServerRpc(serverInfo.getServer_id(),rpcUrl);
        }

        buildTypeServerId(serverList);
        //获取此类型的服务器列表
        Map<Integer, Integer> typeMap = Maps.newHashMap();
        for (ServerInfo serverInfo : serverList) {
            typeMap.put(serverInfo.getServer_id(), serverInfo.getType());
        }
        for (ServerInfo serverInfo : serverList) {
            int dbId = serverInfo.getDb_id() != 0 && serverInfo.getDb_id() != serverInfo.getServer_id() ? serverInfo.getDb_id() : serverInfo.getServer_id();
            if (typeMap.containsKey(dbId)) {
                groupMap.put(serverInfo.getServer_id(), typeMap.get(dbId));
            } else {
                log.error("服务器加载地址出错:" + serverInfo.getServer_id());
            }
        }
        this.groupMap = ImmutableMap.copyOf(groupMap);
    }

    public void buildTypeServerId(List<ServerInfo> serverList) {
        ArrayListMultimap<Integer, Integer> typeServerIdMap = ArrayListMultimap.create();
        for (ServerInfo serverInfo : serverList) {
            if (serverInfo.getDb_id() == 0 || serverInfo.getDb_id() == serverInfo.getServer_id()) {
                typeServerIdMap.put(serverInfo.getType(), serverInfo.getServer_id());
            }
        }
        this.typeServerIdMap = typeServerIdMap;
    }

    public int getGroupId(long playerId) {
        int createServerId = ServerUtils.getCreateServerId(playerId);
        return this.groupMap.getOrDefault(createServerId, -1);
    }

    public List<Integer> getSeverIds(int gameType) {
        return this.typeServerIdMap.get(gameType);
    }

    public Set<Integer> getAllGameTypes() {
        return Sets.newHashSet(this.groupMap.values());
    }
}
