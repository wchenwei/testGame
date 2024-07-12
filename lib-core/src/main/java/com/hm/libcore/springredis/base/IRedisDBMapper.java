package com.hm.libcore.springredis.base;

import com.hm.libcore.springredis.util.RedisMapperUtil;

public interface IRedisDBMapper<T> {
    T getId();

    int getServerId();

    default void saveDB() {
        RedisMapperUtil.update(this);
    }

    default void delete() {
        RedisMapperUtil.delete(this);
    }

    default boolean isExist(){
        return RedisMapperUtil.isExist(this);
    }

}
