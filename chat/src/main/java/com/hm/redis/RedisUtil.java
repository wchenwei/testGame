package com.hm.redis;

import com.hm.libcore.util.gson.GSONUtils;

public class RedisUtil {

    public static PlayerRedisData getPlayerRedisData(long playerId) {
        String val = RedisTypeEnum.Player.get(String.valueOf(playerId));
        return GSONUtils.FromJSONString(val, PlayerRedisData.class);
    }

    //阵营信息
    public static GuildRedisData getGuildRedisData(int guildId) {
        return GSONUtils.FromJSONString(RedisTypeEnum.Guild.get(String.valueOf(guildId)), GuildRedisData.class);
    }

    public static ServerRedisData getServerRedisData(int serverId) {
        String val = RedisTypeEnum.ServerData.get(String.valueOf(serverId));
        return GSONUtils.FromJSONString(val, ServerRedisData.class);
    }

}
