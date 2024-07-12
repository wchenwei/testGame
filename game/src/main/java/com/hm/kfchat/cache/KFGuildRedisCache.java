package com.hm.kfchat.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hm.redis.data.GuildRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 緩存管理器
 *
 * @author xiaoaogame
 */
@Slf4j
public class KFGuildRedisCache {

    private static final KFGuildRedisCache instance = new KFGuildRedisCache();

    public static KFGuildRedisCache getInstance() {
        return instance;
    }

    /**
     * 5分钟后重新请求
     */
    protected static final String CACHESPEC = "refreshAfterWrite=300m";
    private final LoadingCache<Integer, GuildRedisData> cache;
    public final GuildRedisData DefaultValue;

    private KFGuildRedisCache() {
        DefaultValue = new GuildRedisData();
        DefaultValue.setId(-1);
        cache = CacheBuilder.from(CACHESPEC)
                .maximumSize(2000)
                .build(new CacheLoader<Integer, GuildRedisData>() {
                    @Override
                    public GuildRedisData load(Integer key) {
                        GuildRedisData temp = getGuildRedisData(key);
                        return temp == null ? DefaultValue : temp;
                    }
                });
    }

    public static GuildRedisData getGuildRedisData(Integer key) {
        long startTime = System.currentTimeMillis();
        GuildRedisData temp = RedisUtil.getGuildRedisData(key);
        long diffTime = System.currentTimeMillis() - startTime;
        if (diffTime > 1000) {
            log.error("获取guild信息异常:" + key + "=" + diffTime);
        }
        return temp;
    }

    public GuildRedisData getGuild(int guildId) {
        GuildRedisData player = cache.getUnchecked(guildId);
        return player.getId() == -1 ? null : player;
    }

    public void clearAllCache() {
        cache.invalidateAll();
    }

}
