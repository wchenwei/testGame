package com.hm.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDbUtil {

    private static JedisPool jedisPool;

    //获取链接
    public static synchronized Jedis getJedis() {
        if (jedisPool == null) {
            //Policy.setPolicy(new AWSPolicy());
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            //指定连接池中最大空闲连接数
            jedisPoolConfig.setMaxIdle(10);
            //链接池中创建的最大连接数
            jedisPoolConfig.setMaxTotal(50);
            //设置创建链接的超时时间
            jedisPoolConfig.setMaxWaitMillis(2000);
            //表示连接池在创建链接的时候会先测试一下链接是否可用，这样可以保证连接池中的链接都可用的。
            /*
	    	String ipAddress = "127.0.0.1";
	        int port = 6379;
	        jedisPoolConfig.setTestOnBorrow(true);
	        jedisPool = new JedisPool(jedisPoolConfig, ipAddress, port, 4000);*/

            jedisPoolConfig.setTestOnBorrow(true);

            jedisPool = new JedisPool(jedisPoolConfig, RedisConfig.getInstance().getHost(),
                    RedisConfig.getInstance().getPort(), 4000, RedisConfig.getInstance().getPassword(), RedisConfig.getInstance().getDbIndex());
        }
        Jedis jedis = jedisPool.getResource();

        return jedis;
    }

    //返回链接
    public static void returnResource(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }

    // 出现异常释放资源  
    public synchronized void returnBrokenResource(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }
}
