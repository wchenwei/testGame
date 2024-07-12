package com.hm.redis.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.RedisKeyUtils;
import com.hm.rediscenter.MongoRedisUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import redis.clients.jedis.Jedis;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeadData {
    private long playerId;
    // 头像id
    private int icon;
    // 失效时间
    private long endTime;

    public void saveRedis() {
        try (Jedis jedis = MongoRedisUtils.getJedis()){
            jedis.sadd(RedisKeyUtils.buildKeyName("_HeadData"), GSONUtils.ToJSONString(this));
        }
    }

    public void removeRedis(){
        try (Jedis jedis = MongoRedisUtils.getJedis()){
            jedis.srem(RedisKeyUtils.buildKeyName("_HeadData"), GSONUtils.ToJSONString(this));
        }
    }

    public static List<HeadData> queryAll(){
        List<HeadData> list = Lists.newArrayList();
        try (Jedis jedis = MongoRedisUtils.getJedis()){
            jedis.smembers(RedisKeyUtils.buildKeyName("_HeadData")).forEach(str->{
                list.add(JSON.toJavaObject(JSONObject.parseObject(str), HeadData.class));
            });
        }
        return list;
    }
}
