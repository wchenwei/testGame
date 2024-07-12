package com.hm.model.cityworld;

import com.hm.enums.CityStatusType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 城镇状态 
 * @author siyunlong  
 * @date 2019年1月17日 上午10:22:55 
 * @version V1.0
 */
public class CityStatus extends CityComponent{
	//状态时间
	private ConcurrentHashMap<Integer, Long> staticMap = new ConcurrentHashMap<>();
	
	public boolean haveCityStatus(CityStatusType statusType) {
		return getCityStatus(statusType) > 0;
	}
	public long getCityStatus(CityStatusType statusType) {
		long endTime = this.staticMap.getOrDefault(statusType.getType(), 0L);
		if(endTime > 0 && endTime < System.currentTimeMillis()) {
			this.staticMap.remove(statusType.getType());
			SetChanged();
			return 0L;
		}
		return endTime;
	}
	
	public void putCityStatus(CityStatusType statusType,long endTime) {
		long oldTime = getCityStatus(statusType);
		if(oldTime > endTime) {
			return;
		}
		this.staticMap.put(statusType.getType(), endTime);
		SetChanged();
	}
	
	public void clearData() {
		this.staticMap.clear();
		SetChanged();
	}
}
