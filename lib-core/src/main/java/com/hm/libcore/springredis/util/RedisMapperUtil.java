package com.hm.libcore.springredis.util;

import com.google.common.collect.ArrayListMultimap;
import com.hm.libcore.springredis.RedisKeyUtils;
import com.hm.libcore.springredis.base.IRedisDBMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.libcore.springredis.factory.MapperFactory;
import com.hm.libcore.springredis.mapper.AbstractRedisMapper;

import java.util.List;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-04-20 15:59
 **/
public class RedisMapperUtil {

    public static String getKey(Object serverId, Object primarykey, String collName) {
        return RedisKeyUtils.buildKeyName(collName + "_" + serverId + "_" + primarykey);
    }

    /**
     * 查询
     *
     * @param serverId
     * @param id
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> T queryOne(Object serverId, Object id, Class<T> entityClass) {
        AbstractRedisMapper redisMapper = getRedisMapper(entityClass);
        return redisMapper.queryOne(serverId, id, entityClass, isZip(entityClass));
    }


    /**
     * 批量查询
     *
     * @param serverId
     * @param primKeys
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> List<T> queryListByPrimkeys(Object serverId, List<Object> primKeys, Class<T> entityClass) {
        AbstractRedisMapper redisMapper = getRedisMapper(entityClass);
        return redisMapper.queryListByPrimkeys(serverId, primKeys, entityClass, isZip(entityClass));
    }

    /**
     * 查询全部
     *
     * @param serverId
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> List<T> queryListAll(Object serverId, Class<T> entityClass) {
        AbstractRedisMapper redisMapper = getRedisMapper(entityClass);
        return redisMapper.queryListAll(serverId, entityClass, isZip(entityClass));
    }

    /**
     * 保存更新
     *
     * @param entity
     * @param <T>
     */
    public static <T extends IRedisDBMapper> void update(T entity) {
        AbstractRedisMapper redisMapper = getRedisMapper(entity);
        redisMapper.update(entity, isZip(entity));
    }

    /**
     * 删除
     *
     * @param entity
     * @param <T>
     */
    public static <T extends IRedisDBMapper> void delete(T entity) {
        AbstractRedisMapper redisMapper = getRedisMapper(entity);
        redisMapper.delete(entity.getServerId(), entity.getId(), entity.getClass());
    }

    /**
     * 是否存在
     * @param entity
     * @param <T>
     */
    public static <T extends IRedisDBMapper> boolean isExist(T entity) {
        AbstractRedisMapper redisMapper = getRedisMapper(entity);
        return redisMapper.isExist(entity.getServerId(), entity.getId(), entity.getClass());
    }

    public static <T> boolean isExist(Object serverId, Object id, Class<T> entityClass) {
        AbstractRedisMapper redisMapper = getRedisMapper(entityClass);
        return redisMapper.isExist(serverId, id, entityClass);
    }

    /**
     * 批量删除
     *
     * @param serverId
     * @param entityClass
     * @param <T>
     */
    public static <T extends IRedisDBMapper> void deleteList(Object serverId, List<Object> primKeys, Class<T> entityClass) {
        AbstractRedisMapper redisMapper = getRedisMapper(entityClass);
        redisMapper.deleteList(serverId, primKeys, entityClass);
    }

    /**
     * 批量删除全部
     *
     * @param serverId
     * @param entityClass
     * @param <T>
     */
    public static <T extends IRedisDBMapper> void deleteAll(Object serverId, Class<T> entityClass) {
        AbstractRedisMapper redisMapper = getRedisMapper(entityClass);
        redisMapper.deleteAll(serverId, entityClass);
}

    /**
     * 批量保存更新
     *
     * @return false 更新失败 true 成功
     * @see MapperType#STRING_HASH 时可用 其他类型暂不支持
     * 使用时注意！！！
     */
    public static <T extends IRedisDBMapper> boolean updateAll(Object serverId, List<T> entityList, Class<T> entityClass) {
        AbstractRedisMapper redisMapper = getRedisMapper(entityClass);
        return redisMapper.updateAll(serverId, entityList, entityClass, isZip(entityClass));
    }


    public static <T extends IRedisDBMapper> boolean updateAllByGroup(Object serverId, List<T> entityList) {
        ArrayListMultimap<Class,T> classMap = ArrayListMultimap.create();
        for (T t : entityList) {
            classMap.put(t.getClass(),t);
        }
        for (Class aClass : classMap.keySet()) {
            updateAll(serverId,classMap.get(aClass),aClass);
        }
        return true;
    }

    public static <T> AbstractRedisMapper getRedisMapper(Class<T> entity) {
        RedisMapperType annotation = getRedisMapperType(entity);
        return MapperFactory.getFactory().getRedisMapper(annotation.type(), annotation.db());
    }

    public static <T> AbstractRedisMapper getRedisMapper(T entity) {
        RedisMapperType annotation = getRedisMapperType(entity.getClass());
        return MapperFactory.getFactory().getRedisMapper(annotation.type(), annotation.db());
    }

    public static <T> boolean isZip(Class<T> entity) {
        RedisMapperType annotation = getRedisMapperType(entity);
        return annotation.isZip();
    }

    public static <T> boolean isZip(T entity) {
        RedisMapperType annotation = getRedisMapperType(entity.getClass());
        return annotation.isZip();
    }

    public static <T> RedisMapperType getRedisMapperType(Class<T> classz) {
        RedisMapperType annotation = classz.getAnnotation(RedisMapperType.class);
        if (annotation != null) {
            return annotation;
        }
        return classz.getSuperclass().getAnnotation(RedisMapperType.class);
    }
}

