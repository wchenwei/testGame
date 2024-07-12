package com.hm.action.kf.kfbuild;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.RedisKeyUtils;
import com.hm.rediscenter.MongoRedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

public class HunterRedisUtils {
	public static final String KeyName = "CrystalHunterLog";
	
	
	public static void saveDB(CrystalHunterLog log) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String json = GSONUtils.ToJSONString(log);
			jedis.hset(buildKey(log.getTypeId()), log.getId(), json);
		}
	}
	
	public static List<CrystalHunterLog> getAllCrystalHunterLog(int typeId) {
		List<CrystalHunterLog> logList = Lists.newArrayList();
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			Map<String, String> results = jedis.hgetAll(buildKey(typeId));
			for (String temp : results.values()) {
				logList.add(GSONUtils.FromJSONString(temp, CrystalHunterLog.class));
			}
		}
		return logList;
	}
	
	public static void removeTypeGroup(int typeId,List<CrystalHunterLog> removeList) {
		if(CollUtil.isEmpty(removeList)) {
			return;
		}
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String[] delIds = removeList.stream().map(e -> e.getId()).toArray(String[]::new);
			jedis.hdel(buildKey(typeId), delIds);
		}
	}
	
	public static String buildKey(int typeId) {
		return RedisKeyUtils.buildKeyName(KeyName+typeId);
	}
	
}
