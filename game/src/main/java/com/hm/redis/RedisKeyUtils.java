package com.hm.redis;

import com.hm.libcore.mongodb.MongoUtils;

public class RedisKeyUtils {
	public static String buildKeyName(String key) {
		return MongoUtils.getGameDBName()+key;
	}
}
