package com.hm.redis;

import com.hm.libcore.mongodb.MongoUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

//存放数据。无序的数据
public enum RedisTypeEnum {
    Player("player", "玩家"),
    Guild("guild", "军团信息"),
    BadWord("badWord", "(后台强制敏感词，出一次就封)"),
    ReportBadWord("reportBadWord", "(后台敏感词,比如5分钟内发了多少次封)"),
    ForbidKeyWord("forbidWord", "敏感词过滤，原配置表forbid_word"),
    ServerData("ServerData", "服务器数据"),
    GameInnerIp("GameInnerIp", "外网ip对应的内网ip"),

    ;

    private RedisTypeEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    private String key;
    private String desc;

    //增加到redis中，1表示新增，0表示更新
    //此方法是用于存储带serverid区分的hash，
    public long put(int serverId, String field, String value) {
        Jedis jedis = RedisDbUtil.getJedis();
        long result = jedis.hset(this.getId(serverId), field, value);
        RedisDbUtil.returnResource(jedis);
        return result;
    }

    //此方法是存储不区分server的数据。
    public long put(String field, String value) {
        Jedis jedis = RedisDbUtil.getJedis();
        long result = jedis.hset(this.getKey(), field, value);
        RedisDbUtil.returnResource(jedis);
        return result;
    }

    public long put(int field, String value) {
        return this.put(String.valueOf(field), value);
    }

    public long put(int field, int value) {
        return this.put(String.valueOf(field), String.valueOf(value));
    }

    //从redis获取数据，带serverId区分的
    public String get(int serverId, String field) {
        Jedis jedis = RedisDbUtil.getJedis();
        String value = jedis.hget(this.getId(serverId), field);
        RedisDbUtil.returnResource(jedis);
        return value;
    }

    //从redis后去数据，不带serverid区分的
    public String get(String field) {
        Jedis jedis = RedisDbUtil.getJedis();
        String value = jedis.hget(this.getKey(), field);
        RedisDbUtil.returnResource(jedis);
        return value;
    }

    public byte[] getForData(byte[] key) {
        Jedis jedis = RedisDbUtil.getJedis();
        byte[] value = jedis.hget(this.getKey().getBytes(), key);
        RedisDbUtil.returnResource(jedis);
        return value;
    }

    public String getStr(int field) {
        return this.get(String.valueOf(field));
    }

    public int getInt(int field) {
        return Integer.parseInt(this.get(String.valueOf(field)));
    }

    //从redis获取数据，1表示删除成功，0表示没有此field值
    public long del(int serverId, String field) {
        Jedis jedis = RedisDbUtil.getJedis();
        long result = jedis.hdel(this.getId(serverId), field);
        RedisDbUtil.returnResource(jedis);
        return result;
    }

    public Set<String> getAllKeys() {
        Jedis jedis = RedisDbUtil.getJedis();
        Set<String> strings = jedis.hgetAll(this.getKey()).keySet();
        RedisDbUtil.returnResource(jedis);
        return strings;
    }

    //查询key不拼接游戏标识
    public Set<String> getAllKeysAndNotGameMark() {
        Jedis jedis = RedisDbUtil.getJedis();
        Set<String> strings = jedis.hgetAll(this.key).keySet();
        RedisDbUtil.returnResource(jedis);
        return strings;
    }

    public long del(String field) {
        Jedis jedis = RedisDbUtil.getJedis();
        long result = jedis.hdel(this.getKey(), field);
        RedisDbUtil.returnResource(jedis);
        return result;
    }

    public long del(int field) {
        return this.del(String.valueOf(field));
    }

    //批量放入数据到redis中
    public void putBatch(int serverId, Map<String, String> data) {
        Jedis jedis = RedisDbUtil.getJedis();
        Pipeline pipeLine = jedis.pipelined();
        for (String key : data.keySet()) {
            pipeLine.hset(getId(serverId), key, data.get(key));
        }
        pipeLine.sync();
        RedisDbUtil.returnResource(jedis);
    }

    public void putBatch(Map<String, String> data) {
        Jedis jedis = RedisDbUtil.getJedis();
        Pipeline pipeLine = jedis.pipelined();
        for (String key : data.keySet()) {
            pipeLine.hset(this.getKey(), key, data.get(key));
        }
        pipeLine.sync();
        RedisDbUtil.returnResource(jedis);
    }

    //批量查询redis的数据，（ps:根据排行榜信息）
    public List<Object> getListObj(int serverId, Set<Tuple> tupleSet) {
        Jedis jedis = RedisDbUtil.getJedis();
        Pipeline pipeLine = jedis.pipelined();
        pipeLine.multi();
        for (Tuple tuple : tupleSet) {
            pipeLine.hget(getId(serverId), tuple.getElement());
        }
        Response<List<Object>> data = pipeLine.exec();
        pipeLine.sync();
        RedisDbUtil.returnResource(jedis);
        return data.get();
    }

    @SuppressWarnings("unused")
    private String getDesc() {
        return desc;
    }

    public String getKey() {
        return MongoUtils.getGameDBName() + "_" + key;
    }

    private String getId(int serverId) {
        return String.format("%s_%s", this.getKey(), serverId);
    }
}










