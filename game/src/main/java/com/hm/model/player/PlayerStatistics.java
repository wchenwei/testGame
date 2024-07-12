package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.config.excel.temlate.RechargeGiftTemplate;
import com.hm.enums.ModeStatisticsType;
import com.hm.enums.StatisticsType;
import com.hm.util.ModeStatistics;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 玩家统计数据
 * @author siyunlong  
 * @date 2018年1月3日 下午2:32:29 
 * @version V1.0
 */
public class PlayerStatistics extends PlayerDataContext{
	//此统计不会清空,一直累加
	private ConcurrentHashMap<Integer, Long> lifeStatistics = new ConcurrentHashMap<>(); 
	//此统计会每日重置
	private ConcurrentHashMap<Integer, Long> todayStatistics = new ConcurrentHashMap<>(); 
	//每日模块统计 key
	private ConcurrentHashMap<Integer, ModeStatistics> todayModeStatistics = new ConcurrentHashMap<>();

	//每日模块统计RechargeGiftTemplate ,id, 次数
	private ConcurrentHashMap<Integer, Integer> todayRechargeStatistics = new ConcurrentHashMap<>();
	//每周统计
	private ConcurrentHashMap<Integer, Long> weekStatistics = new ConcurrentHashMap<>();


	//添加累计事件统计
	public synchronized void addLifeStatistics(StatisticsType statisticsType,long num) {
		this.lifeStatistics.put(statisticsType.getType(),getLifeStatistics(statisticsType)+num);
		SetChanged();
	}
	public void addLifeStatistics(StatisticsType statisticsType) {
		addLifeStatistics(statisticsType, 1);
	}
	public long getLifeStatistics(StatisticsType statisticsType) {
		return this.lifeStatistics.getOrDefault(statisticsType.getType(), 0L);
	}
	public long getLifeStatistics(int statisticsType) {
		return this.lifeStatistics.getOrDefault(statisticsType, 0L);
	}
	public void setLifeStatistics(StatisticsType statisticsType,long num) {
		this.lifeStatistics.put(statisticsType.getType(),num);
		SetChanged();
	}

	//添加每日事件统计
	public synchronized void addTodayStatistics(StatisticsType statisticsType,long num) {
		this.todayStatistics.put(statisticsType.getType(),getTodayStatistics(statisticsType)+num);
		SetChanged();
	}
	public void addTodayStatistics(StatisticsType statisticsType) {
		addTodayStatistics(statisticsType, 1);
	}
	public long getTodayStatistics(StatisticsType statisticsType) {
		return this.todayStatistics.getOrDefault(statisticsType.getType(), 0L);
	}
	public synchronized void addTodayModeStatistics(ModeStatisticsType modeType,int key) {
		ModeStatistics temp = this.todayModeStatistics.get(modeType.getType());
		if(temp == null) {
			temp = new ModeStatistics();
			this.todayModeStatistics.put(modeType.getType(), temp);
		}
		temp.addKey(key);
		SetChanged();
	}
	public long getTodayModeStatistics(ModeStatisticsType modeType,int key) {
		ModeStatistics temp = this.todayModeStatistics.get(modeType.getType());
		if(temp == null) return 0;
		return temp.getValue(key);
	}

	public synchronized void addTodayRechargeStatistics(RechargeGiftTemplate rechargeGift) {
		this.todayRechargeStatistics.put(rechargeGift.getId(), this.todayRechargeStatistics.getOrDefault(rechargeGift.getId(), 0)+1);
		SetChanged();
	}

	public boolean checkCanRecharge(RechargeGiftTemplate rechargeGift) {
		if(null==rechargeGift.getLimit_buy()|| rechargeGift.getLimit_buy()==0) {
			return true;
		}
		return rechargeGift.getLimit_buy()>this.todayRechargeStatistics.getOrDefault(rechargeGift.getId(), 0);
	}
	
	//清空每日数据
	public void clearTodayData() {
		this.todayModeStatistics.clear();
		this.todayStatistics.clear();
		this.todayRechargeStatistics.clear();

		long mlv = super.Context().playerCommander().getMilitaryLv();
		long fbId = super.Context().playerMission().getFbId();
		this.todayStatistics.put(StatisticsType.MLv.getType(), mlv);
		this.todayStatistics.put(StatisticsType.FbId.getType(), fbId);

		SetChanged();
	}

	public void clearWeekData() {
		long mlv = super.Context().playerCommander().getMilitaryLv();
		long fbId = super.Context().playerMission().getFbId();
		this.weekStatistics.put(StatisticsType.MLv.getType(), mlv);
		this.weekStatistics.put(StatisticsType.FbId.getType(), fbId);

		SetChanged();
	}

	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerStatistics", this);
	}
}

