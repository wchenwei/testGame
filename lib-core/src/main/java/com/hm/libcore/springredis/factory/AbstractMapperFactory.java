package com.hm.libcore.springredis.factory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.mapper.AbstractRedisMapper;
import lombok.Data;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-09-23 14:30
 **/
@Data
public abstract class AbstractMapperFactory {
    // type - db - factory
    protected Table<Integer, Integer, AbstractRedisMapper> dbTable = HashBasedTable.create();

    public AbstractRedisMapper getRedisMapper(MapperType type, int db) {
        return dbTable.get(type.getType(), db);
    }
}
