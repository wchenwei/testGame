package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.model.cityworld.WorldCity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @Description: 部队克隆数据
 * @author siyunlong  
 * @date 2019年8月7日 下午8:35:58 
 * @version V1.0
 */
public class PlayerCloneTroops extends PlayerDataContext {
	private ConcurrentHashMap<Integer,CityCloneData> cityMap = new ConcurrentHashMap<>();

	public void addCityTroop(WorldCity worldCity) {
		CityCloneData cityCloneData = getCityCloneDataAndCreate(worldCity.getId());
		cityCloneData.addCount(1);
		SetChanged();
	}
	
	public boolean deathCityTroop(int cityId,long exp) {
		CityCloneData cityCloneData = getCityCloneDataAndCreate(cityId);
		if(cityCloneData.getCount() > 0) {
			cityCloneData.addCount(-1);
			cityCloneData.addExp(exp);
			SetChanged();
			return true;
		}
		return false;
	}

	public void deathAllCityTroop(long exp) {
		for (CityCloneData value : cityMap.values()) {
			if(value.getCount() > 0) {
				value.addExp(value.getCount()*exp);
				value.setCount(0);
				SetChanged();
			}
		}
	}
	public void deathCityAllTroop(int cityId,long exp) {
		CityCloneData value = this.cityMap.get(cityId);
		if(value != null && value.getCount() > 0) {
			value.addExp(value.getCount()*exp);
			value.setCount(0);
			SetChanged();
		}
	}
	
	public long rewardExp() {
		long totalExp = 0;
		for (Map.Entry<Integer,CityCloneData> entry : this.cityMap.entrySet()) {
			CityCloneData cityCloneData = entry.getValue();
			totalExp += cityCloneData.getExp();
			cityCloneData.setExp(0);
			if(cityCloneData.isCanDel()) {
				this.cityMap.remove(entry.getKey());
			}
		}
		SetChanged();
		return totalExp;
	}
	
	private CityCloneData getCityCloneDataAndCreate(int cityId) {
		CityCloneData cityCloneData = this.cityMap.get(cityId);
		if(cityCloneData == null) {
			cityCloneData = new CityCloneData();
			this.cityMap.put(cityId, cityCloneData);
		}
		return cityCloneData;
	}
	
	public void removeCloneForCitys(List<Integer> cityIds) {
		for (int cityId : cityIds) {
			CityCloneData cityCloneData = this.cityMap.get(cityId);
			if(cityCloneData != null && cityCloneData.getCount() > 0) {
				cityCloneData.setCount(0);
				SetChanged();
			}
		}
	}
	public long getTotalExp(List<Integer> cityIds,long exp) {
		long totalExp = 0;
		for (int cityId : cityIds) {
			CityCloneData cityCloneData = this.cityMap.remove(cityId);
			if(cityCloneData != null) {
				totalExp += cityCloneData.getExp()+cityCloneData.getCount()*exp;
			}
		}
		SetChanged();
		return totalExp;
	}
	
	public long getTotalExp(long exp) {
		if(this.cityMap.isEmpty()) {
			return 0;
		}
		long totalExp = 0;
		for (CityCloneData cityCloneData : this.cityMap.values()) {
			totalExp += cityCloneData.getExp()+cityCloneData.getCount()*exp;
		}
		this.cityMap.clear();
		SetChanged();
		return totalExp;
	}
	
    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerCloneTroops", this);
    }
}
