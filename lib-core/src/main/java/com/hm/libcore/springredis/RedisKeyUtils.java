package com.hm.libcore.springredis;

import com.hm.libcore.mongodb.MongoUtils;

public class RedisKeyUtils {
    public static String buildKeyName(String key) {
        return MongoUtils.getGameDBName() + "_" + key;
    }
}
