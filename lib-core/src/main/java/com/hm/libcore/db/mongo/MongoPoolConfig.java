package com.hm.libcore.db.mongo;

import com.hm.libcore.spring.SpringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wyp
 * @description
 *      MongoDB 连接池 配置
 * @date 2021/8/19 18:06
 */
@Data
@Component
@ConfigurationProperties(prefix = "poolsetting")
public final class MongoPoolConfig {
    private static MongoPoolConfig instance = null;

    public static MongoPoolConfig getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(MongoPoolConfig.class);
            instance = new MongoPoolConfig();
        }
        return instance;
    }


    /**
     * 允许的最大连接数。 这些连接将在空闲时保留在池中。 一旦池耗尽，任何需要连接的操作都将阻塞，等待可用的连接
     */
    private int maxSize = 100;
    /**
     * 最小连接数。 这些连接将在空闲时保留在池中，池将确保它至少包含这个最小数量
     */
    private int minSize;
    /**
     * 值为0意味着它不会等待。 一个线程等待连接可用的最大时间
     */
    private long maxWaitTime = 2;
    /**
     * 池连接可以存在的最大时间。 零值表示对生命时间没有限制。 已超过其生命周期的池连接将被关闭，并在必要时由新连接替换
     */
    private long maxConnectionLifeTime;
    /**
     * 池连接的最大空闲时间。 零值表示不限制空闲时间。 具有超过它的空闲时间将被关闭，并在必要时由一个新的连接代替。
     */
    private long maxConnectionIdleTime;
    /**
     * 在连接池上运行第一个维护作业之前等待的时间。
     */
    private long maintenanceInitialDelay;
    /**
     * 运行维护作业之间的时间间隔
     */
    private long maintenanceFrequency = 1;


}
