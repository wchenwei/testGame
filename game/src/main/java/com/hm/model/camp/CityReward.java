package com.hm.model.camp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.GuildRewardTimesType;
import com.hm.model.item.Items;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description: 城池奖励
 * @author siyunlong  
 * @date 2019年4月28日 下午2:46:33 
 * @version V1.0
 */
@NoArgsConstructor
@Data
public class CityReward {
	private transient long time;
	private int hour;
	private List<Integer> cityList = Lists.newArrayList();
	private List<Integer> extraCityList = Lists.newArrayList();
	//部落科技加成（GuildRewardTimesType，加成数量）
	private Map<Integer, Double> rewardRate = Maps.newHashMap();
	//额外的资源产出 每个城市都要给
	private List<Items> itemsList = Lists.newArrayList();
	
	public CityReward(int hour,List<Integer> cityList) {
		this.hour = hour;
		this.cityList = cityList;
		this.time = System.currentTimeMillis();
	}
	
	public boolean isFit(long lastTime) {
		return this.time > lastTime;
	}
	
	public void addExtraItems(Items item) {
		this.itemsList.add(item);
	}
	
	public void addRewardRate(GuildRewardTimesType type,double v) {
		this.rewardRate.put(type.getType(), this.rewardRate.getOrDefault(type.getType(), 0d)+v);
	}
	
	public double getRewardRate(GuildRewardTimesType type) {
		return this.rewardRate.getOrDefault(type.getType(), 0d);
	}
	
	public CityRewardShow buildCityRewardShow() {
		return new CityRewardShow(this.hour,this.time,this.cityList);
	}
}
