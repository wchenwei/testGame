package com.hm.libcore.springredis.factory;


import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.config.RedisTemplateConfig;
import com.hm.libcore.springredis.mapper.HashJsonRedisMapper;
import com.hm.libcore.springredis.mapper.StringHashRedisMapper;
import com.hm.libcore.springredis.mapper.StringValueRedisMapper;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.libcore.util.jackson.ObjectMapperBuilder;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-09-14 16:53
 **/
@Data
public class MapperFactory extends AbstractMapperFactory {
    private static final MapperFactory factory = new MapperFactory();

    private MapperFactory() {
    }

    public static MapperFactory getFactory() {
        return factory;
    }

    public void registerDB() {
        Set<Integer> dbList = SpringUtil.getBeanMap(BaseEntityMapper.class)
                .values().stream().map(e -> RedisMapperUtil.getRedisMapperType(e.getClass()))
                .filter(Objects::nonNull)
                .map(e -> e.db()).collect(Collectors.toSet());
        RedisTemplateConfig redisConfig = SpringUtil.getBean(RedisTemplateConfig.class);
        for (int dbIndex : dbList) {
            initDb(redisConfig.buildRedisTemplate(dbIndex), dbIndex);
        }
    }


    private void initDb(RedisTemplate<String, Serializable> redisTemplate0, int db0) {
        buildStringHashMapper(redisTemplate0, db0);
        buildStringValueMapper(redisTemplate0, db0);
        buildHashJsonMapper(redisTemplate0, db0);
    }


    private void buildStringHashMapper(RedisTemplate<String, Serializable> redisTemplate, int db) {
        StringHashRedisMapper stringHashRedisMapper = new StringHashRedisMapper();
        stringHashRedisMapper.setRedisTemplate(redisTemplate);
        dbTable.put(MapperType.STRING_HASH.getType(), db, stringHashRedisMapper);
    }

    private void buildHashJsonMapper(RedisTemplate<String, Serializable> redisTemplate, int db) {
        HashJsonRedisMapper hashJsonRedisMapper = new HashJsonRedisMapper();
        // flatten false 非扁平化
        hashJsonRedisMapper.setHashMapper(new Jackson2HashMapper(ObjectMapperBuilder.buildJacksonObjectMapper(), false));
        hashJsonRedisMapper.setRedisTemplate(redisTemplate);
        dbTable.put(MapperType.HASH_JSON.getType(), db, hashJsonRedisMapper);
    }

    private void buildStringValueMapper(RedisTemplate<String, Serializable> redisTemplate, int db) {
        StringValueRedisMapper stringValueRedisMapper = new StringValueRedisMapper();
        stringValueRedisMapper.setRedisTemplate(redisTemplate);
        dbTable.put(MapperType.STRING_VALUE.getType(), db, stringValueRedisMapper);
    }


}
