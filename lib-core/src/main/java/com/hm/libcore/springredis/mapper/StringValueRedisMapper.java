package com.hm.libcore.springredis.mapper;

import com.google.common.collect.Lists;
import com.hm.libcore.springredis.RedisKeyUtils;
import com.hm.libcore.springredis.base.IRedisDBMapper;
import com.hm.libcore.util.jackson.JackSonUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @description: 按字符串存储
 * @author: chenwei
 * @create: 2020-09-15 10:07
 **/
public class StringValueRedisMapper extends AbstractRedisMapper {

    @Override
    public <T> T queryOne(Object serverId, Object id, Class<T> entityClass, boolean isZip) {
        String key = getKey(serverId, id, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        byte[] data = (byte[]) redisTemplate.opsForValue().get(key);
        return JackSonUtils.byteToObj(data, isZip);
    }

    @Override
    public <T extends IRedisDBMapper> void update(T entity, boolean isZip) {
        String key = getKey(entity.getServerId(), entity.getId(), getCollName(entity.getClass()));
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.opsForValue().set(key, JackSonUtils.objToByte(entity, isZip));
    }

    @Override
    public <T> List<T> queryListByPrimkeys(Object serverId, List<Object> primKeys, Class<T> entityClass, boolean isZip) {
        List<String> keys = getKeys(serverId, primKeys, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        List list = redisTemplate.opsForValue().multiGet(keys);
        List<T> resultList = Lists.newArrayList();
        list.stream().filter(Objects::nonNull).forEach(e -> resultList.add(JackSonUtils.byteToObj((byte[]) e, isZip)));
        return resultList;
    }

    @Override
    public <T> List<T> queryListAll(Object serverId, Class<T> entityClass, boolean isZip) {
        RedisTemplate redisTemplate = getRedisTemplate();
        List<String> keys = getLikeKeys(serverId, getCollName(entityClass));
        List list = redisTemplate.opsForValue().multiGet(keys);
        List<T> resultList = Lists.newArrayList();
        list.stream().filter(Objects::nonNull).forEach(e -> resultList.add(JackSonUtils.byteToObj((byte[]) e, isZip)));
        return resultList;
    }

    @Override
    public <T> void delete(Object serverId, Object id, Class<T> entityClass) {
        String key = getKey(serverId, id, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.delete(key);
    }

    @Override
    public <T> boolean isExist(Object serverId, Object id, Class<T> entityClass) {
        String key = getKey(serverId, id, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> void deleteList(Object serverId, List<Object> ids, Class<T> entityClass) {
        List<String> keys = getKeys(serverId, ids, getCollName(entityClass));
        getRedisTemplate().delete(keys);
    }

    @Override
    public <T extends IRedisDBMapper> void deleteAll(Object serverId, Class<T> entityClass) {
        List<String> keys = getLikeKeys(serverId, getCollName(entityClass));
        getRedisTemplate().delete(keys);
    }


    public static String getKey(Object serverId, Object primKey, String collName) {
        return RedisKeyUtils.buildKeyName(collName + "_" + serverId + "_" + primKey);
    }

    public static List<String> getKeys(Object serverId, List<Object> primarykeys, String collName) {
        List<String> keys = Lists.newArrayList();
        for (Object primarykey : primarykeys) {
            keys.add(getKey(serverId, primarykey, collName));
        }
        return keys;
    }

}
