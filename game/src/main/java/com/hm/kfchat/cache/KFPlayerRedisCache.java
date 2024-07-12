package com.hm.kfchat.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 緩存管理器
 *
 * @author xiaoaogame
 */
@Slf4j
public class KFPlayerRedisCache {

    private static final KFPlayerRedisCache instance = new KFPlayerRedisCache();

    public static KFPlayerRedisCache getInstance() {
        return instance;
    }

    /**
     * 5分钟后重新请求
     */
    protected static final String CACHESPEC = "refreshAfterWrite=30m";
    private final LoadingCache<Long, PlayerRedisData> cache;
    public final PlayerRedisData DefaultValue;

    private KFPlayerRedisCache() {
        DefaultValue = new PlayerRedisData();
        DefaultValue.setId(-1);
        cache = CacheBuilder.from(CACHESPEC)
                .maximumSize(4000)
                .build(new CacheLoader<Long, PlayerRedisData>() {
                    @Override
                    public PlayerRedisData load(Long key) {
                        PlayerRedisData temp = getRedisPlayer(key);
                        return temp == null ? DefaultValue : temp;
                    }
                });
    }

    public static PlayerRedisData getRedisPlayer(Long key) {
        long startTime = System.currentTimeMillis();
        PlayerRedisData temp = RedisUtil.getPlayerRedisData(key);
        long diffTime = System.currentTimeMillis() - startTime;
        if (diffTime > 1000) {
            log.error("获取redis玩家信息异常:" + key + "=" + diffTime);
        }
        return temp;
    }

    public PlayerRedisData getPlayer(long playerId) {
        PlayerRedisData player = cache.getUnchecked(playerId);
        return player.getId() == -1 ? null : player;
    }

    public void removePlayerCache(long playerId) {
        cache.invalidate(playerId);
    }

    public void clearAllCache() {
        cache.invalidateAll();
    }

    public static void main(String[] args) {
        System.err.println(KFPlayerRedisCache.getInstance().getPlayer(200001));
    }
}
