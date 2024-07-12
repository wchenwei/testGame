package com.hm.redis.mode;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.RedisKeyUtils;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.rediscenter.MongoRedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: hash数据存储基类
 * @author siyunlong  
 * @date 2020年12月9日 下午5:56:24 
 * @version V1.0
 */
public abstract class AbstractRedisHashMode {
	
	public AbstractRedisHashMode() {
	}
	
	public abstract String buildFiledKey();
	public abstract String buildHashKey();

	public void saveDB() {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String json = GSONUtils.ToJSONString(this);
			jedis.hset(RedisKeyUtils.buildKeyName(buildHashKey()), buildFiledKey(), json);
		}
	}
	public void del() {
		delHashField(RedisKeyUtils.buildKeyName(buildHashKey()), buildFiledKey());
	}
	
	public static <T extends AbstractRedisHashMode> List<T> getHashList(String hashKey,Class<T> entityClass) {
		List<T> resultList = Lists.newArrayList();
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			for (String val : jedis.hgetAll(RedisKeyUtils.buildKeyName(hashKey)).values()) {
				resultList.add(GSONUtils.FromJSONString(val, entityClass));
			}
		}
		return resultList;
	}
	
	public static <T extends AbstractRedisHashMode> T getHashVal(String hashKey,String fieldKey,Class<T> entityClass) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String json = jedis.hget(RedisKeyUtils.buildKeyName(hashKey), fieldKey);
			if(StrUtil.isNotEmpty(json)) {
				return GSONUtils.FromJSONString(json, entityClass);
			}
		}
		return null;
	}

	public static <T extends AbstractRedisHashMode> List<T> getHashValForKeyList(String hashKey,List<Long> fieldKeyList,Class<T> entityClass) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			return jedis.hmget(RedisKeyUtils.buildKeyName(hashKey), RedisTypeEnum.listToStrArrays(fieldKeyList))
					.stream().map(e -> GSONUtils.FromJSONString(e, entityClass))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		}
	}
	
	public static void delHashField(String hashKey,String fieldKey) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			jedis.hdel(hashKey, fieldKey);
		}
	}
}
