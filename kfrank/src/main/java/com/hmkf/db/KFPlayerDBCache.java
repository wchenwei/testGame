package com.hmkf.db;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hmkf.model.KFPlayer;

/**
 * 緩存管理器
 *
 * @author xiaoaogame
 */
public class KFPlayerDBCache {
    private static final KFPlayerDBCache instance = new KFPlayerDBCache();

    public static KFPlayerDBCache getInstance() {
        return instance;
    }

    /**
     * 记录玩家的侦查
     */
    protected static final String CACHESPEC = "expireAfterAccess=5m";
    public final KFPlayer DefaultValue;
    private final LoadingCache<Long, KFPlayer> cache;

    private KFPlayerDBCache() {
        DefaultValue = new KFPlayer();
        DefaultValue.setId(-1L);
        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(10, TimeUnit.MINUTES)//设置时间对象没有被读/写访问则对象从内存中删除
                .expireAfterWrite(10, TimeUnit.MINUTES)//设置时间对象没有被写访问则对象从内存中删除
                .build(new CacheLoader<Long, KFPlayer>() {

                    @Override
                    public KFPlayer load(Long key) {
                        KFPlayer player = KfDBUtils.getPlayerFromDB(key, KFPlayer.class);
                        return player == null ? DefaultValue : player;
                    }
                });
    }

    public KFPlayer getPlayer(long playerId) {
        KFPlayer player = cache.getUnchecked(playerId);
        return player.getId() == DefaultValue.getId() ? null : player;
    }

    public void removePlayerCache(long playerId) {
        cache.invalidate(playerId);
    }

    public void clearAllCache() {
        cache.invalidateAll();
    }

    public void addPlayerToCache(KFPlayer player) {
        cache.put(player.getId(), player);
    }

}
