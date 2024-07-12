package com.hm.libcore.springredis.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.db.mongo.ClassChanged;
import com.hm.libcore.springredis.RedisKeyUtils;
import com.hm.libcore.springredis.base.IRedisDBMapper;
import com.hm.libcore.util.jackson.JackSonUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @description: 每条数据按非扁平化 json存储
 * @author: chenwei
 * @create: 2020-09-15 10:06
 **/
public class HashJsonRedisMapper extends AbstractRedisMapper {

    @Override
    public Jackson2HashMapper getHashMapper() {
        return (Jackson2HashMapper) super.getHashMapper();
    }

    @Override
    public <T> T queryOne(Object serverId, Object id, Class<T> entityClass, boolean isZip) {
        String key = getKey(serverId, id, getCollName(entityClass));
        return queryOne(key, isZip);
    }

    @Override
    public <T> List<T> queryListByPrimkeys(Object serverId, List<Object> primKeys, Class<T> entityClass, boolean isZip) {
        List<T> list = Lists.newArrayList();
        if (CollUtil.isNotEmpty(primKeys)) {
            primKeys.forEach(id -> {
                T t = queryOne(serverId, id, entityClass, isZip);
                if (t != null) {
                    list.add(t);
                }
            });
        }
        return list;
    }

    @Override
    public <T> List<T> queryListAll(Object serverId, Class<T> entityClass, boolean isZip) {
        List<T> list = Lists.newArrayList();
        List<String> likeKeys = getLikeKeys(serverId, getCollName(entityClass));
        for (String likeKey : likeKeys) {
            T t = queryOne(likeKey, isZip);
            if (t != null) {
                list.add(t);
            }
        }
        return list;
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
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.delete(keys);
    }

    @Override
    public <T extends IRedisDBMapper> void deleteAll(Object serverId, Class<T> entityClass) {
        List<String> matchKeys = getLikeKeys(serverId, getCollName(entityClass));
        getRedisTemplate().delete(matchKeys);
    }


    @Override
    public <T extends IRedisDBMapper> void update(T entity, boolean isZip) {
        try {
            synchronized (entity) {
                Jackson2HashMapper mapper = getHashMapper();
                HashOperations<String, String, byte[]> hashOperations = getRedisTemplate().opsForHash();
                String key = getKey(entity.getServerId(), entity.getId(), getCollName(entity.getClass()));
                Field[] var6 = ReflectUtil.getFields(entity.getClass());
                int var7 = var6.length;
                Map<String, Object> mappedHash = mapper.toHash(entity);
                for (int var8 = 0; var8 < var7; ++var8) {
                    Field field = var6[var8];
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (field.getAnnotation(Transient.class) != null) {
                        mappedHash.remove(fieldName);
                        continue;
                    }
                    Object obj = field.get(entity);
                    if (obj != null) {
                        if (obj instanceof ClassChanged) {
                            ClassChanged changeObj = (ClassChanged) obj;
                            if (!changeObj.Changed()) {
                                mappedHash.remove(fieldName);
                            } else {
                                changeObj.ClearChangedFlag();
                            }
                        }
                    }
                }
                Map<String, byte[]> byteMap = JackSonUtils.objToByteMap(mappedHash, isZip);
                hashOperations.putAll(key, byteMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private <T> T queryOne(String key, boolean isZip) {
        try {
            Jackson2HashMapper mapper = getHashMapper();
            HashOperations<String, String, byte[]> hashOperations = getRedisTemplate().opsForHash();
            Map<String, byte[]> mappedHash = hashOperations.entries(key);
            Map<String, Object> objectMap = JackSonUtils.byteToObjMap(mappedHash, isZip);
            if (CollUtil.isNotEmpty(mappedHash)) {
                return (T) mapper.fromHash(objectMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getKey(Object serverId, Object primarykey, String collName) {
        return RedisKeyUtils.buildKeyName(collName + "_" + serverId + "_" + primarykey);
    }

    public static List<String> getKeys(Object serverId, List<Object> primarykeys, String collName) {
        List<String> keys = Lists.newArrayList();
        for (Object primarykey : primarykeys) {
            keys.add(getKey(serverId, primarykey, collName));
        }
        return keys;
    }

}
