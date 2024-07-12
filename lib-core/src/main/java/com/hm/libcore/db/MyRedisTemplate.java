package com.hm.libcore.db;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MyRedisTemplate<K,V> extends RedisTemplate {

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public long hincr(String key, String item, long by) {
        return this.opsForHash().increment(key, item, by);
    }
    public void zincr(String key,String item,long by){
        this.opsForZSet().incrementScore(key,item,by);
    }

    public long incr(String key, long by) {
        return this.opsForValue().increment(key, by);
    }
    public void saveValue(String key, String value, long l, TimeUnit timeUnit) {
        if (l <= 0) {
            this.opsForValue().set(key, value);
        } else {
            this.opsForValue().set(key, value, l, timeUnit);
        }
    }
    public String getValue(String key, long l, TimeUnit timeUnit) {
        String ret = (String) this.opsForValue().get(key);
        if (ret != null && l > 0) {
            this.expire(key, l, timeUnit);
        }
        return ret;
    }
    //获取
    public String getValue(String key) {
        return (String) this.opsForValue().get(key);
    }
    //删除
    public void delValue(String key) {
        this.opsForValue().getOperations().delete(key);
    }
    //保存hash
    public void saveHashValue(String key, String hashKey, String value) {
        HashOperations<String, String, String> ops = this.opsForHash();
        ops.put(key, hashKey, value);
    }

    public String getHashValue(String key, String hashKey) {
        HashOperations<String, String, String> ops = this.opsForHash();
        return ops.get(key, hashKey);
    }

    public List<String> getMultiHashValue(String key, Collection<String> hashKey) {
        HashOperations<String, String, String> ops = this.opsForHash();
        return ops.multiGet(key, hashKey);
    }

    public void delHashValue(String key, String hashKey) {
        HashOperations<String, String, String> ops = this.opsForHash();
        ops.delete(key, hashKey);
    }

    public void saveSValue(String key, String value) {
        this.opsForSet().add(key, value);
    }

    public Set<String> getRndValue(String key, int count) {
        return this.opsForSet().distinctRandomMembers(key, count);
    }

    public void delSValue(String key, String value) {
        this.opsForSet().remove(key, value);
    }

    public boolean isSContain(String key, String value) {
        return this.opsForSet().isMember(key, value);
    }
    //设置过期时间
    public void setExpire(String key, long time, TimeUnit timeUnit) {
        this.expire(key, time, timeUnit);
    }

    //设置过期时间
    public void setExpireAt(String key, Date date){
        expireAt(key,date);
    }
}
