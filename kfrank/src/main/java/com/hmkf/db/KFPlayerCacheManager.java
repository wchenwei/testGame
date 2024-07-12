package com.hmkf.db;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hm.model.player.Player;

/**
 * 緩存管理器
 *
 * @author xiaoaogame
 */
public class KFPlayerCacheManager {
    private static final KFPlayerCacheManager instance = new KFPlayerCacheManager();

    public static KFPlayerCacheManager getInstance() {
        return instance;
    }

    /**
     * 5分钟后重新请求
     */
    protected static final String CACHESPEC = "refreshAfterWrite=5m";
    public final Player DefaultValue;
    private final LoadingCache<Long, Player> cache;

    private KFPlayerCacheManager() {
        DefaultValue = new Player();
        DefaultValue.setId(-1L);
        cache =
                //CacheBuilder.from(CACHESPEC)
                CacheBuilder.newBuilder()
                        .maximumSize(500)
                        .expireAfterAccess(10, TimeUnit.MINUTES)//设置时间对象没有被读/写访问则对象从内存中删除
                        .expireAfterWrite(10, TimeUnit.MINUTES)//设置时间对象没有被写访问则对象从内存中删除
                        .recordStats()
                        .build(new CacheLoader<Long, Player>() {

                            @Override
                            public Player load(Long key) {
                                Player player = KfPlayerUtils.getPlayerFromDB2(key);
                                return player == null ? DefaultValue : player;
                            }
                        });
    }

    public Player getPlayer(long playerId) {
        Player player = cache.getUnchecked(playerId);
        return player.getId() == DefaultValue.getId() ? null : player;
    }

    public void removePlayerCache(long playerId) {
        cache.invalidate(playerId);
    }

    public void clearAllCache() {
        cache.invalidateAll();
    }

    public LoadingCache<Long, Player> getCache() {
        return cache;
    }

    public void addPlayerToCache(Player player) {
        cache.put(player.getId(), player);
    }

}
