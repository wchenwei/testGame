package com.hm.model.activity.kfseason.server;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SesaonGodWarPlayer {
	private long playerId;
	private String name;
	private long score;//战功
	
	public SesaonGodWarPlayer(long playerId, long score) {
		super();
		this.playerId = playerId;
		this.score = score;
		loadName();
	}
	
	public void loadName() {
		PlayerRedisData redisData = RedisUtil.getPlayerRedisData(this.playerId);
		this.name = redisData.getName();
	}
}
