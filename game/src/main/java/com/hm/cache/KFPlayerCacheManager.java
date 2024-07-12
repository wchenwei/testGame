package com.hm.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.model.player.Player;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.util.ServerUtils;

import java.util.concurrent.TimeUnit;

/**
 * 緩存管理器
 * @author xiaoaogame
 *
 */
public class KFPlayerCacheManager {
	private static final KFPlayerCacheManager instance = new KFPlayerCacheManager();
	public static KFPlayerCacheManager getInstance() {
		return instance;
	}
	/**
	 * 记录玩家的侦查
	 */
	protected static final String CACHESPEC = "expireAfterAccess=5m";
	public final Player DefaultValue;
	private final LoadingCache<Long, Player> cache;

	private KFPlayerCacheManager() {
        DefaultValue = new Player();
        DefaultValue.setId(-1L);
        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(5, TimeUnit.MINUTES)//设置时间对象没有被读/写访问则对象从内存中删除
        		.build(new CacheLoader<Long, Player>() {

            @Override
            public Player load(Long playerId) {
				Player player = getKFPlayerFromBD(playerId);
                return player == null ? DefaultValue : player;
            }
        });
    }
	
	public Player getKFPlayer(long playerId) {
		Player player = cache.getUnchecked(playerId);
        return player.getId() == DefaultValue.getId() ? null : player;
    }

	public Player getKFPlayerFromBD(long playerId) {
		ServerInfo serverInfo = ServerUtils.getServerInfo(ServerUtils.getCreateServerId(playerId));
		int serverId = serverInfo.getDb_id() > 0?serverInfo.getDb_id():serverInfo.getServer_id();
		MongodDB mongo = MongoUtils.getGameMongodDB(serverId);
		if(mongo == null) {
			System.err.println("kf mongo is err:"+playerId);
			return DefaultValue;
		}
		return mongo.get(playerId, Player.class);
	}
}
