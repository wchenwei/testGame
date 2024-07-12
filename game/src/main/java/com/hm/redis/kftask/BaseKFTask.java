package com.hm.redis.kftask;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.config.GameConstants;
import com.hm.rediscenter.MongoRedisUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Map;

/**
 * 基础跨服任务数据
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/4/9 10:38
 */
@Data
@NoArgsConstructor
public abstract class BaseKFTask {
    private KFTaskType type;
    private String id;
    private Map<Integer, Long> eventMap = Maps.newConcurrentMap();

    public BaseKFTask(KFTaskType type, Number id) {
        this.type = type;
        this.id = id + "";
    }


    public void addEvent(KFTaskEventType type, long add) {
        this.eventMap.put(type.getType(), getEventVal(type.getType()) + add);
    }

    public void resetEvent(KFTaskEventType type, long val) {
        this.eventMap.put(type.getType(), val);
    }

    public long getEventVal(int type) {
        return this.eventMap.getOrDefault(type, 0L);
    }


    public void saveRedis() {
        try (Jedis jedis = MongoRedisUtils.getJedis()) {
            String json = GSONUtils.ToJSONString(this);
            jedis.hset(type.buildKeyName(), getId(), json);
        }
    }

    public static String getVal(String key, Number playerId) {
        try (Jedis jedis = MongoRedisUtils.getJedis()) {
            return jedis.hget(key, playerId + "");
        }
    }


    public static void delRedis(KFTaskType taskType) {
        try {
            try (Jedis jedis = MongoRedisUtils.getJedis()) {
                String key = taskType.buildKeyName();
                String newKey = key + ":" + createMark();

                Map<String, String> oldMap = jedis.hgetAll(key);
                jedis.hmset(newKey, oldMap);
                jedis.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createMark() {
        return DateUtil.format(new Date(System.currentTimeMillis() - GameConstants.DAY), DatePattern.PURE_DATE_FORMAT);
    }

    public int getIntType() {
        return this.type.getType();
    }


}
