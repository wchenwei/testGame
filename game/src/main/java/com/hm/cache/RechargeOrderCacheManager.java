package com.hm.cache;

import cn.hutool.core.util.StrUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * @Description: 充值订单缓存
 * @author siyunlong  
 * @date 2020年4月17日 下午3:14:40 
 * @version V1.0
 */
@Slf4j
public class RechargeOrderCacheManager {
	private static final RechargeOrderCacheManager instance = new RechargeOrderCacheManager();
	public static RechargeOrderCacheManager getInstance() {
		return instance;
	}

	protected static final String CACHESPEC = "expireAfterWrite=10m";
	private final LoadingCache<String, String> cache;
	
	private RechargeOrderCacheManager() {
		cache = CacheBuilder.from(CACHESPEC)
			.maximumSize(300)
			.build(new CacheLoader<String, String>() {
			@Override
			public String load(String key) {
				return "";
			}
		});
	}
	
	public boolean orderIsExist(String orderId) {
		try {
			if(StrUtil.isEmpty(orderId)) {
				return true;
			}
			return StrUtil.isNotEmpty(cache.get(orderId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void addOrderId(String orderId) {
		this.cache.put(orderId, "1");
	}
	
	public static void main(String[] args) {
		RechargeOrderCacheManager manager = RechargeOrderCacheManager.getInstance();
		System.err.println(manager.orderIsExist("1"));
		manager.addOrderId("1");
		System.err.println(manager.orderIsExist("1"));
	}
}
