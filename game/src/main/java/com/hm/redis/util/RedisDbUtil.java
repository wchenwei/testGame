package com.hm.redis.util;

import com.hm.redis.config.RedisConfig;
import com.hm.redis.type.RedisConstants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDbUtil {
	private static JedisPool[] jedisPools = new JedisPool[RedisConstants.MaxIndex];
	static{
		for (int i = 0; i < jedisPools.length; i++) {
			jedisPools[i] = getJedisPool(i);
		}
	}

	//获取链接
	public static Jedis getJedis(int index) {
		return jedisPools[index].getResource();
	}

    public static JedisPool getJedisPool(int index){
    	JedisPool jedisPool;
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
        		RedisConfig.getInstance().getPort(), 4000, RedisConfig.getInstance().getPassword(), getDbIndex(index));
        return jedisPool;
    }

    public static int getDbIndex(int index) {
        return Integer.valueOf(RedisConfig.getInstance().getDbs().split(",")[index]);
    }
    
    //返回链接
    public static void returnResource(Jedis jedis){
    	if (null != jedis) {  
    		jedis.close();
    	}
    }
    
    public static JedisPool getJedisPoolByIndex(int index) {
		return jedisPools[index];
	}
    
    // 出现异常释放资源  
    public void returnBrokenResource(Jedis jedis) {  
        if (null != jedis) {  
        	jedis.close();
        }  
    }  
}
