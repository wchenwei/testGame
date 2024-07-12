package com.hm.db;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.redis.RedisUtil;
import com.hm.redis.ServerRedisData;
import com.hm.libcore.rpc.GameRpcClientUtils;
import com.hm.utils.GameIpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class GameServerManager {
    private static GameServerManager instance = new GameServerManager();

    public static GameServerManager getInstance() {
        return instance;
    }

    //此服务器运行的游戏服务器id列表
    private Map<Integer, ServerRedisData> serverMap = Maps.newHashMap();

    public List<Integer> getServerList() {
        return ImmutableList.copyOf(serverMap.keySet());
    }

    public void init() {
        serverMap = getAllServerIdListFromDB().stream()
            .map(RedisUtil::getServerRedisData)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(ServerRedisData::getId, Function.identity()));
        log.info("游戏服：" + this.serverMap.keySet());
    }

    /**
     * 包括合服的服务器
     *
     * @param serverId
     * @return
     */
    public boolean containServer(int serverId) {
        return this.serverMap.containsKey(serverId);
    }

    public int getServerOpenDay(int serverId) {
        ServerRedisData serverData = serverMap.get(serverId);
        if (serverData == null) {
            serverData = RedisUtil.getServerRedisData(serverId);
            if(serverData == null) {
                log.error("服务器[{}]没有开服数据", serverId);
                return 1;
            }
            serverMap.put(serverId,serverData);
        }
        return serverData.getOpenDay();
    }

    /**
     * 获取所有配置为当前聊天服的游戏服id列表
     *
     * @return
     */
    public static List<Integer> getAllServerIdListFromDB() {
        List<GameServerMachine> machineList = getChatServerMachineList();
        Map<Integer,GameServerMachine> machineMap = machineList.stream().collect(Collectors.toMap(GameServerMachine::getId,e->e));

        List<ServerInfo> list = getAllServerInfoFromDB(machineList);
        for (ServerInfo serverInfo : list) {
            GameServerMachine serverMachine = machineMap.get(serverInfo.getServermachineId());
            String rpcUrl = GameIpUtils.getGameRpcUrl(serverMachine);
            GameRpcClientUtils.changeServerRpc(serverInfo.getServer_id(),rpcUrl);
        }
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(ServerInfo::getServer_id).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public static List<ServerInfo> getAllServerInfoFromDB(List<GameServerMachine> machineList) {
        List<Integer> machineIds = machineList.stream().map(e -> e.getId()).collect(Collectors.toList());
        Criteria criteria = Criteria.where("servermachineId").in(machineIds);
        return MongoUtils.getLoginMongodDB().query(Query.query(criteria), ServerInfo.class);
    }



    /**
     * 获取配置为当前聊天服的游戏物理机
     *
     * @return
     */
    public static List<GameServerMachine> getChatServerMachineList() {
        int chatserverId = ServerConfig.getInstance().getMachineId();
        Criteria criteria = Criteria.where("chatserver").is(chatserverId);
        return MongoUtils.getLoginMongodDB().query(Query.query(criteria), GameServerMachine.class);
    }
}

