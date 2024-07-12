package com.hm.redis;

import com.hm.redis.type.RedisConstants;
import com.hm.redis.util.RedisDbUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.UUID;

/**
 * 
 * @Description: redis实现分布式锁
 * @author siyunlong  
 * @date 2019年7月31日 下午8:22:04 
 * @version V1.0
 */
public class RedisLockTool {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final String SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    private static final Long RELEASE_SUCCESS = 1L;
    
    /**
     * 获取请求Id 
     * @return 返回UUID
     */
    public static String getRequestId(){
        return UUID.randomUUID().toString();
    }
    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间 单位毫秒
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
    	Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
    	try {
    		//如果当前不存在key,则设置key值+失效时间
            String result = jedis.set(lockKey, requestId, SetParams.setParams().nx().ex(expireTime));
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
    }
    
    
    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(String lockKey, String requestId) {
    	Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
    	try {
    		Object result = jedis.eval(SCRIPT, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
    }
    
    public static void testDistributedLock() {
        for (int i = 0; i < 100; i++) {
        	final int index = i;
        	try {
        		Thread.sleep(1000);
    			System.err.println(index+"============");
			} catch (Exception e) {
				e.printStackTrace();
			}
            new Thread(new Runnable() {
            	
                @Override
                public void run() {
                    String lockKey = "lock";
                    String requestId = getRequestId();
                    // 加锁
                    if (tryGetDistributedLock(lockKey, requestId, 2000)) {
                    	System.out.println(index+"获得锁");
                    	try {
                    		Thread.sleep(100000);
            			} catch (Exception e) {
            				e.printStackTrace();
            			}
                        System.out.println(index+"="+requestId + ":完成");
                        // 解锁
                        releaseDistributedLock(lockKey, requestId);
                    } else {
                        System.out.println(index+"="+requestId + ": tryGetDistributedLock fail");
                    }

                }
            }).start();
        }
    }
    
    public static void main(String[] args) {
        RedisLockTool.testDistributedLock();
    }
}