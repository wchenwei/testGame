package com.hm.action.http.gm;

import com.google.common.collect.Maps;
import com.hm.libcore.db.mongo.DBEntity;

import java.util.Map;

public class LvDistribution extends DBEntity<String>{
	private Map<Integer, Long> lvMap = Maps.newConcurrentMap();
	private long onlineTimes;//总在线时长

	public LvDistribution() {
		super();
	}

	public LvDistribution(String id) {
		super();
		this.setId(id);
	}

	public Map<Integer, Long> getLvMap() {
		return lvMap;
	}
	
	public void setLvMap(Map<Integer, Long> lvMap) {
		this.lvMap = lvMap;
	}

	public void addLv(int lv ,long num){
		this.lvMap.put(lv, num);
	}

	public long getOnlineTimes() {
		return onlineTimes;
	}

	public void setOnlineTimes(long onlineTimes) {
		this.onlineTimes = onlineTimes;
	}

}
