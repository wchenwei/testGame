package com.hm.model.serverpublic.kf;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.ServerNameCache;
import com.hm.redis.util.RedisUtil;
import com.hm.util.ServerUtils;
import com.hm.util.bluevip.QQBlueVip;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KFPlayerRankData extends KfPlayerInfo{
	private int serverId;
	private String serverName;
	private int rank;
	private double score;
    private QQBlueVip blueVip;

    public KFPlayerRankData(int serverId, String serverName, int rank, double score) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.rank = rank;
        this.score = score;
    }

	public static KFPlayerRankData buildKFPlayerRankData(long playerId) {
		int serverId = ServerUtils.getCreateServerId(playerId);
		String serverName = ServerNameCache.getServerName(serverId);
		PlayerRedisData redisData = RedisUtil.getPlayerRedisData(playerId);
		KFPlayerRankData kfPlayerData = new KFPlayerRankData(serverId, serverName, 0, 0);
		kfPlayerData.load(redisData);
		return kfPlayerData;
	}

    @Override
    public void load(PlayerRedisData redisData) {
        super.load(redisData);
//        this.blueVip = redisData.getBlueVip();
    }
}
