package com.hm.cache;

import com.google.common.base.Ticker;
import com.google.common.cache.*;

import java.util.concurrent.TimeUnit;

/**
 * 回收的参数：
　　1. 大小的设置：CacheBuilder.maximumSize(long)  CacheBuilder.weigher(Weigher)  CacheBuilder.maxumumWeigher(long)
　　2. 时间：expireAfterAccess(long, TimeUnit) expireAfterWrite(long, TimeUnit)
　　3. 引用：CacheBuilder.weakKeys() CacheBuilder.weakValues()  CacheBuilder.softValues()
　　4. 明确的删除：invalidate(key)  invalidateAll(keys)  invalidateAll()
　　5. 删除监听器：CacheBuilder.removalListener(RemovalListener)
　　refresh机制：
　　1. LoadingCache.refresh(K)  在生成新的value的时候，旧的value依然会被使用。
　　2. CacheLoader.reload(K, V) 生成新的value过程中允许使用旧的value
　　3. CacheBuilder.refreshAfterWrite(long, TimeUnit) 自动刷新cache
 * @author xiaoaogame
 *
 */
public class GoogleCache<K,T> {
	private Cache<K,T> cache;
	public GoogleCache(String CACHESPEC) {
		CacheBuilder<K,T> cacheBuilder = (CacheBuilder<K, T>) CacheBuilder.from(CACHESPEC);
		this.cache = cacheBuilder.build();
	}
	
	public void put(K key,T value) {
		this.cache.put(key, value);
	}
	
	public T get(K key) {
		return this.cache.getIfPresent(key);
	}
	
	/**
	 * 自动刷新
	 * @param duration
	 * @param unit
	 */
//	public void refreshAfterWrite(int duration,TimeUnit unit) {
//		this.cacheBuilder.refreshAfterWrite(duration, unit);
//	}
	
	public static void main(String[] args) {
		LoadingCache<String, Integer> loadingCache =
                CacheBuilder.newBuilder()
                    .expireAfterAccess(5L, TimeUnit.SECONDS) //5分钟后缓存失效
                    .maximumSize(5000L) //最大缓存5000个对象
                    .removalListener(new RemovalListener<String, Integer>() {
						@Override
						public void onRemoval(RemovalNotification<String, Integer> arg0) {
							System.out.println("移除："+arg0.getKey());
						}
					}) //注册缓存对象移除监听器
                    .ticker(Ticker.systemTicker()) //定义缓存对象失效的时间精度为纳秒级
                    .build(new CacheLoader<String, Integer>(){ 
                        @Override
                        public Integer load(String key) throws Exception {
                        	System.out.println("获取："+key);
                            return key.length();
                        }
                    });
		try {
			System.out.println(loadingCache.get("aa"));
			Thread.sleep(6000l);
//			System.out.println(loadingCache.get("aa"));
		} catch (Exception e) {
		}
	}
	
}
