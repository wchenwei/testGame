package com.hm.cache;

import com.google.common.cache.*;
import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import com.hm.mongoqueue.MongoQueue;

import java.util.concurrent.TimeUnit;

/**
 * 緩存管理器
 * @author xiaoaogame
 *
 */
public class PlayerCacheManager {
	private static final PlayerCacheManager instance = new PlayerCacheManager();
	public static PlayerCacheManager getInstance() {
		return instance;
	}
	/**
	 * 记录玩家的侦查
	 */
	protected static final String CACHESPEC = "expireAfterAccess=5m";
	public final Player DefaultValue;
	private final LoadingCache<Long, Player> cache;
	
	private PlayerCacheManager() {
        DefaultValue = new Player();
        DefaultValue.setId(-1L);
        cache = CacheBuilder.newBuilder()
                .maximumSize(6000)
                .expireAfterAccess(20, TimeUnit.MINUTES)//设置时间对象没有被读/写访问则对象从内存中删除
                .recordStats()
                .removalListener(new RemovalListener<Long, Player>() {
					@Override
					public void onRemoval(RemovalNotification<Long, Player> notification) {
						doRemoveFoSave(notification.getValue());
						System.err.println("cache del player "+notification.getKey());
					}
				})
        		.build(new CacheLoader<Long, Player>() {

            @Override
            public Player load(Long key) {
            	Player player = PlayerUtils.getPlayerFromDB(key);
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
	
	public Player getPlayerOrNull(long playerId) {
		try {
			return cache.getIfPresent(playerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void doRemoveFoSave(Player player) {
		if(MongoQueue.NoOnlineSaveOpen && player.isDBChangeMark()) {
			player.saveNowDB();
		}
	}
	
	
	public void addPlayerToCache(Player player) {
		cache.put(player.getId(), player);
	}

	public LoadingCache<Long, Player> getCache() {
		return cache;
	}
	
}
