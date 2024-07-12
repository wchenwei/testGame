package com.hm.libcore.springredis.mapper;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.springredis.RedisKeyUtils;
import com.hm.libcore.springredis.base.IRedisDBMapper;
import com.hm.libcore.util.jackson.JackSonUtils;
import com.hm.libcore.util.string.StringUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 全部数据按hash存储
 * @author: chenwei
 * @create: 2020-09-15 10:07
 **/
public class StringHashRedisMapper extends AbstractRedisMapper {

    @Override
    public <T> T queryOne(Object serverId, Object id, Class<T> entityClass, boolean isZip) {
        String key = getKey(serverId, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        byte[] data = (byte[]) redisTemplate.opsForHash().get(key, String.valueOf(id));
        return JackSonUtils.byteToObj(data, isZip);
    }

    @Override
    public <T extends IRedisDBMapper> void update(T entity, boolean isZip) {
        String key = getKey(entity.getServerId(), getCollName(entity.getClass()));
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.opsForHash().put(key, String.valueOf(entity.getId()), JackSonUtils.objToByte(entity, isZip));
    }

    @Override
    public <T extends IRedisDBMapper> void update(T entity, Object groupId, boolean isZip) {
        String key = getKey(groupId, getCollName(entity.getClass()));
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.opsForHash().put(key, String.valueOf(entity.getId()), JackSonUtils.objToByte(entity, isZip));
    }

    @Override
    public <T> List<T> queryListByPrimkeys(Object serverId, List<Object> primKeys, Class<T> entityClass, boolean isZip) {
        String key = getKey(serverId, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        List list = redisTemplate.opsForHash().multiGet(key, StringUtil.objList2StringList(primKeys));
        List<T> resultList = Lists.newArrayList();
        list.stream().filter(Objects::nonNull).forEach(e -> resultList.add(JackSonUtils.byteToObj((byte[]) e, isZip)));
        return resultList;
    }

    @Override
    public <T> List<T> queryListAll(Object serverId, Class<T> entityClass, boolean isZip) {
        String key = getKey(serverId, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        List list = redisTemplate.opsForHash().values(key);
        List<T> resultList = Lists.newArrayList();
        list.stream().filter(Objects::nonNull).forEach(e -> resultList.add(JackSonUtils.byteToObj((byte[]) e, isZip)));
        return resultList;
    }

    @Override
    public <T> void delete(Object serverId, Object id, Class<T> entityClass) {
        String key = getKey(serverId, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.opsForHash().delete(key, String.valueOf(id));
    }

    @Override
    public <T> boolean isExist(Object serverId, Object id, Class<T> entityClass) {
        String key = getKey(serverId, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        return redisTemplate.opsForHash().hasKey(key, String.valueOf(id));
    }

    @Override
    public <T> void deleteList(Object serverId, List<Object> ids, Class<T> entityClass) {
        String key = getKey(serverId, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.opsForHash().delete(key, StringUtil.objList2StringArray(ids));
    }

    @Override
    public <T extends IRedisDBMapper> void deleteAll(Object serverId, Class<T> entityClass) {
        String key = getKey(serverId, getCollName(entityClass));
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.delete(key);
    }

    @Override
    public <T extends IRedisDBMapper> boolean updateAll(Object serverId, List<T> entityList, Class<T> entityClass, boolean isZip) {
        if (CollUtil.isNotEmpty(entityList)) {
            String key = getKey(serverId, getCollName(entityClass));
            Map<String, Object> objectMap = Maps.newHashMap();
            entityList.forEach(e -> objectMap.put(String.valueOf(e.getId()), e));
            Map<String, byte[]> byteMap = JackSonUtils.objToByteMap(objectMap, isZip);
            getRedisTemplate().opsForHash().putAll(key, byteMap);
            return true;
        }
        return false;
    }

    public static String getKey(Object serverId, String collName) {
        return RedisKeyUtils.buildKeyName(collName + "_" + serverId);
    }
}
