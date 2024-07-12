package com.hm.libcore.mongodb;

import com.google.common.util.concurrent.AtomicLongMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: mongo数据库每分钟数据统计
 * @author siyunlong  
 * @date 2020年10月29日 下午5:02:08 
 * @version V1.0
 */
public class MongoMonitorUtils {
	private static DateFormat MMDDHHMM = new SimpleDateFormat("MMddHHmm");
	public static boolean isClose = true; 
	public static AtomicLongMap<String> countMap = AtomicLongMap.create();
	
	public static void addObjCount(Object obj) {
		try {
			if(obj == null || isClose) {
				return;
			}
			addCount(obj.getClass().getSimpleName());
		} catch (Exception e) {
		}
	}
	
	public static void addCount(String key) {
		try {
			if(isClose) {
				return;
			}
			countMap.incrementAndGet(buildMinuteKey(key));
		} catch (Exception e) {
		}
	}
	public static void addKeyCount(String key) {
		try {
			if(isClose) {
				return;
			}
			addCount("key:"+key);
		} catch (Exception e) {
		}
	}
	
	public static void showData() {
		if(isClose) {
			return;
		}
//		List<String> keys = Lists.newArrayList(countMap.asMap().keySet());
//		Collections.sort(keys);
		System.err.println(countMap.toString());
	}
	
	public static String buildMinuteKey(String key) {
		return MMDDHHMM.format(new Date())+":"+key;
	}
}
