package com.hm.libcore.springredis.mapper;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.springredis.RedisKeyUtils;
import com.hm.libcore.springredis.base.IRedisDBMapper;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import lombok.Data;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.hash.HashMapper;

import java.util.List;

@Data
public abstract class AbstractRedisMapper {

    private RedisTemplate redisTemplate;
    private HashMapper hashMapper;
    // 默认每次扫描100条
    public static final int DEFAULT_SCAN_COUNT = 100;

    /**
     * 查询单个值
     */
    public abstract <T> T queryOne(Object serverId, Object id, Class<T> entityClass, boolean isZip);

    /**
     * 根据主键查询列表
     */
    public abstract <T> List<T> queryListByPrimkeys(Object serverId, List<Object> primKeys, Class<T> entityClass, boolean isZip);

    /**
     * 查询全部
     */
    public abstract <T> List<T> queryListAll(Object serverId, Class<T> entityClass, boolean isZip);

    /**
     * 更新
     */
    public abstract <T extends IRedisDBMapper> void update(T entity, boolean isZip);

    /**
     * 删除单个值
     */
    public abstract <T> void delete(Object serverId, Object id, Class<T> entityClass);

    /**
     * 是否存在
     */
    public abstract <T> boolean isExist(Object serverId, Object id, Class<T> entityClass);

    /**
     * 删除多个值
     */
    public abstract <T> void deleteList(Object serverId, List<Object> ids, Class<T> entityClass);

    /**
     * 删除全部
     */
    public abstract <T extends IRedisDBMapper> void deleteAll(Object serverId, Class<T> entityClass);

    /**
     * 批量更新 部分存储类型可用 MapperType#STRING_HASH
     *
     * @param entityList
     * @param isZip
     * @param <T>
     */
    public <T extends IRedisDBMapper> boolean updateAll(Object serverId, List<T> entityList, Class<T> entityClass, boolean isZip) {
        throw new UnsupportedOperationException("updateAll is not supported this class mapperType");
    };

    /**
     * 更新
     */
    public <T extends IRedisDBMapper> void update(T entity, Object groupId, boolean isZip) {
        throw new UnsupportedOperationException("update is not supported this class mapperType");
    };



    /**
     * 模糊查询key
     *
     * @param serverId
     * @param collName
     * @return
     */
    public List<String> getLikeKeys(Object serverId, String collName) {
        String pattern = getKeyPattern(serverId, collName);
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(DEFAULT_SCAN_COUNT).build();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);
        List<String> keys = Lists.newArrayList();
        while (cursor.hasNext()) {
            keys.add(StrUtil.str(cursor.next(), "utf-8"));
        }
        return keys;
    }

    public String getKeyPattern(Object serverId, String collName) {
        return RedisKeyUtils.buildKeyName(collName + "_" + serverId + "_" + "?");
    }

    /**
     * 获取表名
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> String getCollName(Class<T> entityClass) {
        RedisMapperType annotation = RedisMapperUtil.getRedisMapperType(entityClass);
        if (annotation != null && StrUtil.isNotEmpty(annotation.collName())) {
            return annotation.collName();
        }
        return entityClass.getSimpleName();
    }

}
