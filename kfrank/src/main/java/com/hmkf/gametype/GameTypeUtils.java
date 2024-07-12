package com.hmkf.gametype;

import java.util.List;
import java.util.stream.Collectors;

import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.server.GameServerType;

import cn.hutool.core.util.StrUtil;

public class GameTypeUtils {
    /**
     * 获取当前资源服务器支持的服务器类型列表
     *
     * @return
     */
    public static List<Integer> getGameServerType() {
        String ip = ServerConfig.getInstance().getHostName();
        int port = ServerConfig.getInstance().getPort();
        int httpport = ServerConfig.getInstance().getHttpPort();
        String mineUrl = ip + "#" + port + "#" + httpport;
        return MongoUtils.getLoginMongodDB().queryAll(GameServerType.class, "servertype")
                .stream().filter(e -> e.getStagematchUrl() != null)
                .filter(e -> e.getStagematchUrl().contains(mineUrl))
                .map(e -> e.getId())
                .collect(Collectors.toList());
    }

    public static List<GameServerType> getAllServerTypes() {
        String ip = ServerConfig.getInstance().getHostName();
        System.err.println("===========ip====================:" + ip);
        List<GameServerType> typeList = MongoUtils.getLoginMongodDB().queryAll(GameServerType.class, "servertype");
        List<GameServerType> serverTypeList = typeList.stream().filter(e -> StrUtil.startWith(e.getStagematchUrl(), ip))
                .collect(Collectors.toList());
        return serverTypeList;
    }

    public static int getLeaderIndex(long playerId) {
        return 1;
    }
}
