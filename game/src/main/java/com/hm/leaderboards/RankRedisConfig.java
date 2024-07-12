package com.hm.leaderboards;

import com.hm.libcore.spring.SpringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 中心redis配置
 * @date 2019年11月21日 上午11:06:31
 */
@Data
@Component
@ConfigurationProperties(prefix = "rankredis")
public class RankRedisConfig {
    private static RankRedisConfig instance = null;

    public static RankRedisConfig getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(RankRedisConfig.class);
        }
        return instance;
    }


    private String host;
    private int port;
    private String pd;
    private JedisPool jedisPool;


    public JedisPool getRankPool() {
        if (jedisPool == null) {
            jedisPool = createJedisPool(0);
        }
        return jedisPool;
    }

    private JedisPool createJedisPool(int index) {
        return new JedisPool(buildRedisConfig(), host, port, 4000, pd, index);
    }

    /**
     * maxTotal	资源池中的最大连接数	8	参见关键参数设置建议
     * maxIdle	资源池允许的最大空闲连接数	8	参见关键参数设置建议
     * minIdle	资源池确保的最少空闲连接数	0	参见关键参数设置建议
     * blockWhenExhausted	当资源池用尽后，调用者是否要等待。只有当值为true时，下面的maxWaitMillis才会生效。	true	建议使用默认值。
     * maxWaitMillis	当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。	-1（表示永不超时）	不建议使用默认值。
     * testOnBorrow	向资源池借用连接时是否做连接有效性检测（ping）。检测到的无效连接将会被移除。	false	业务量很大时候建议设置为false，减少一次ping的开销。
     * testOnReturn	向资源池归还连接时是否做连接有效性检测（ping）。检测到无效连接将会被移除。	false	业务量很大时候建议设置为false，减少一次ping的开销。
     * jmxEnabled	是否开启JMX监控	true	建议开启，请注意应用本身也需要开启。
     * testWhileIdle	是否开启空闲资源检测。	false	true
     * timeBetweenEvictionRunsMillis	空闲资源的检测周期（单位为毫秒）	-1（不检测）	建议设置，周期自行选择，也可以默认也可以使用下方JedisPoolConfig 中的配置。
     * minEvictableIdleTimeMillis	资源池中资源的最小空闲时间（单位为毫秒），达到此值后空闲资源将被移除。	180000（即30分钟）	可根据自身业务决定，一般默认值即可，也可以考虑使用下方JeidsPoolConfig中的配置。
     * numTestsPerEvictionRun	做空闲资源检测时，每次检测资源的个数。	3	可根据自身应用连接数进行微调，如果设置为 -1，就是对所有连接做空闲监测。
     *
     * @return
     */
    public JedisPoolConfig buildRedisConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //指定连接池中最大空闲连接数
        jedisPoolConfig.setMaxIdle(10);
        //链接池中创建的最大连接数
        jedisPoolConfig.setMaxTotal(100);
        //设置创建链接的超时时间
        jedisPoolConfig.setMaxWaitMillis(2000);

//        jedisPoolConfig.setTestOnBorrow(false);
//        jedisPoolConfig.setTestOnReturn(false);
//        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(10*1000);
//        jedisPoolConfig.setMinEvictableIdleTimeMillis(60*1000);

        return jedisPoolConfig;
    }

    //返回链接
    public void returnResource(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }

    public void initResource() {
        returnResource(getRankPool().getResource());
    }
}
