package com.hm.libcore.springredis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hm.libcore.db.MyRedisTemplate;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;

/**
 * @description: redis配置
 * @author: chenwei
 * @create: 2020-04-20 14:42
 **/
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisTemplateConfig {
    private String host;
    private int port;
    private String password;
    private int dbIndex;
    private int thresholdDbIndex;
    private String redisDbIndex;


    public RedisTemplate<String, Serializable> buildRedisTemplate(int dbIndex) {
        return redisTemplateObject(dbIndex);
    }

    private RedisTemplate<String, Serializable> redisTemplateObject(Integer dbIndex) {
        RedisTemplate<String, Serializable> redisTemplateObject = new RedisTemplate<String, Serializable>();
        redisTemplateObject.setConnectionFactory(redisConnectionFactory(jedisPoolConfig(), dbIndex));
        setSerializer(redisTemplateObject);
        redisTemplateObject.afterPropertiesSet();
        return redisTemplateObject;
    }

    public MyRedisTemplate<String, String> createStrTemplate(Integer dbIndex) {
        MyRedisTemplate<String, String> redisTemplateObject = new MyRedisTemplate<String, String>();
        redisTemplateObject.setConnectionFactory(redisConnectionFactory(jedisPoolConfig(), dbIndex));
        setStrSerializer(redisTemplateObject);
        redisTemplateObject.afterPropertiesSet();
        return redisTemplateObject;
    }

    private void setStrSerializer(RedisTemplate<String, String> template) {
        Jackson2JsonRedisSerializer<byte[]> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<byte[]>(byte[].class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setKeySerializer(template.getStringSerializer());
        template.setValueSerializer(template.getStringSerializer());
        template.setHashKeySerializer(template.getStringSerializer());
        template.setHashValueSerializer(template.getStringSerializer());
    }

    /**
     * 连接池配置信息
     *
     * @return
     */
    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(200);
        poolConfig.setMinIdle(0);
        poolConfig.setMaxWaitMillis(2000);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    /**
     * jedis连接工厂
     *
     * @param jedisPoolConfig
     * @param db
     * @return
     */
    private RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig, int db) {
        // 单机版
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setDatabase(db);
        configuration.setPassword(RedisPassword.of(password));
        configuration.setPort(port);
        // 获得默认的连接池构造器
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        // 指定jedisPoolConifig来修改默认的连接池构造器
        jpcb.poolConfig(jedisPoolConfig);
        // 通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        return new JedisConnectionFactory(configuration, jedisClientConfiguration);

    }


    private void setSerializer(RedisTemplate<String, Serializable> template) {
        Jackson2JsonRedisSerializer<byte[]> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<byte[]>(byte[].class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setKeySerializer(template.getStringSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(template.getStringSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
    }

    public int getDb() {
        return dbIndex;
    }

    public int getThresholdDbIndex() {
        return thresholdDbIndex;
    }

    public String getRedisDbIndex(){
        return redisDbIndex;
    }
}
