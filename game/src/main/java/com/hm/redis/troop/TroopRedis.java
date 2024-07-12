package com.hm.redis.troop;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.rediscenter.MongoRedisUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import redis.clients.jedis.Jedis;

import java.util.List;

@Data
@NoArgsConstructor
public class TroopRedis {
	private transient TroopRedisType type;
	private String id;
	private List<Integer> tankIds;
	
	public TroopRedis(TroopRedisType type) {
		super();
		this.type = type;
	}
	
	public TroopRedis build(long playerId,List<Integer> tankIds) {
		this.id = playerId+"";
		this.tankIds = tankIds;
		return this;
	}
	
	public boolean isContainsTank(int tankId) {
		return tankIds != null && tankIds.contains(tankId);
	}
	
	public void saveRedis() {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			if(CollUtil.isEmpty(tankIds)) {
				jedis.hdel(buildKeyName(type), getId());
			}else{
				String json = GSONUtils.ToJSONString(this);
				jedis.hset(buildKeyName(type), getId(), json);
			}
		}
	}
	
	public static String buildKeyName(TroopRedisType type) {
		return MongoUtils.getGameDBName()+"_troopredis"+type.getType();
	}

}
