package com.hm.model.guild;

import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.StatisticsType;

import java.util.concurrent.ConcurrentHashMap;

public class GuildStatistics extends GuildComponent{
	//此统计一直累加
	private ConcurrentHashMap<Integer, Long> lifeStatistics = new ConcurrentHashMap<>(); 
	//此统计会每日重置
	private ConcurrentHashMap<Integer, Long> todayStatistics = new ConcurrentHashMap<>(); 

	//添加累计事件统计
	public void addLifeStatistics(StatisticsType statisticsType,long num) {
		this.lifeStatistics.put(statisticsType.getType(),getLifeStatistics(statisticsType)+num);
		SetChanged();
	}
	public void addLifeStatistics(StatisticsType statisticsType) {
		addLifeStatistics(statisticsType, 1);
	}
	//添加每日事件统计
	public void addTodayStatistics(StatisticsType statisticsType,long num) {
		this.todayStatistics.put(statisticsType.getType(),getTodayStatistics(statisticsType)+num);
		SetChanged();
	}
	public void addTodayStatistics(StatisticsType statisticsType) {
		addTodayStatistics(statisticsType, 1);
	}
	
	public long getLifeStatistics(StatisticsType statisticsType) {
		return this.lifeStatistics.getOrDefault(statisticsType.getType(), 0L);
	}
	public long getTodayStatistics(StatisticsType statisticsType) {
		return this.todayStatistics.getOrDefault(statisticsType.getType(), 0L);
	}
	
	public boolean doDayReset() {
		if(!this.todayStatistics.isEmpty()) {
			this.todayStatistics.clear();
			SetChanged();
			return true;
		}
		return false;
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("guildStatistics", this);
	}
}
