package com.hm.redis;

import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.pro.ProUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//redis的配置加载
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
    private static RedisConfig instance = null;

    public static RedisConfig getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(RedisConfig.class);
        }
        return instance;
    }

    private String host;
    private int port;
    private String password;
    private int dbIndex;
}
