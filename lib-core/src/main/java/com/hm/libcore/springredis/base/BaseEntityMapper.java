package com.hm.libcore.springredis.base;


import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * @description: 抽象基础类 需被继承
 * @author: chenwei
 * @create: 2020-09-25 14:23
 **/
@Getter
@Setter
public abstract class BaseEntityMapper<T> extends DBEntity<T> implements IRedisDBMapper {
    @Transient
    private transient boolean isDel;//是否已经被删除

    @Override
    public void delete() {
        this.isDel = true;
        RedisMapperUtil.delete(this);
    }

    @Override
    public void saveDB() {
        if(isDel) return;
        RedisMapperUtil.update(this);
    }

    public static <T> List<T> queryListAll(int serverId, Class<T> entity) {
        return RedisMapperUtil.queryListAll(serverId, entity);
    }

    public static <T> List<T> queryListByPrimkeys(Object serverId, List<Object> primKeys, Class<T> entityClass) {
        return RedisMapperUtil.queryListByPrimkeys(serverId, primKeys, entityClass);
    }

    public static <T> T queryOne(Object serverId, Object id, Class<T> entity) {
        return RedisMapperUtil.queryOne(serverId, id, entity);
    }

    public static <T extends IRedisDBMapper> void deleteAll(int serverId, Class<T> entity) {
        RedisMapperUtil.deleteAll(serverId, entity);
    }

    public static <T> List<T> queryList(Object serverId, List<Object> ids, Class<T> entity) {
        return RedisMapperUtil.queryListByPrimkeys(serverId, ids, entity);
    }

    public static <T> boolean isExist(Object serverId, Object id, Class<T> entity){
        return RedisMapperUtil.isExist(serverId, id, entity);
    }
}
