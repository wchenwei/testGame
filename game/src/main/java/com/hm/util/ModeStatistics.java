package com.hm.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 模块统计
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author siyunlong  
 * @date 2018年7月30日 下午5:01:28 
 * @version V1.0
 */
public class ModeStatistics {
	private ConcurrentHashMap<Integer,Long> data = new ConcurrentHashMap<>(); 
	
	public void addKey(int key,int num) {
		this.data.put(key, getValue(key)+num);
	}
	
	public void addKey(int key) {
		addKey(key, 1);
	}
	
	public long getValue(int key) {
		return this.data.getOrDefault(key, 0L);
	}
}
