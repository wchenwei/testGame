package com.hm.redis.mode;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.RedisKeyUtils;
import com.hm.rediscenter.MongoRedisUtils;
import redis.clients.jedis.Jedis;

public abstract class AbstractRedisKeyMode {
	private String key;
	
	public AbstractRedisKeyMode(String key) {
		this.key = key;
	}

	public void saveDB() {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String json = GSONUtils.ToJSONString(this);
			jedis.set(RedisKeyUtils.buildKeyName(key), json);
		}
	}
	
	public static <T extends AbstractRedisKeyMode> T getVal(String key,Class<T> entityClass) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String result = jedis.get(RedisKeyUtils.buildKeyName(key));
			if(StrUtil.isNotEmpty(result)) {
				return GSONUtils.FromJSONString(result, entityClass);
			}
		}
		return null;
	}
}
