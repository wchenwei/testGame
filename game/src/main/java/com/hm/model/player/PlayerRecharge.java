package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.enums.RechargeType;
import com.hm.model.recharge.RechargeLogWarn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 玩家充值信息
 * @author siyunlong  
 * @date 2018年1月29日 下午4:42:03 
 * @version V1.0
 */
public class PlayerRecharge extends PlayerDataContext{
	//有时效的月卡 周卡
	private ConcurrentHashMap<Integer, Long> yukaMap = new ConcurrentHashMap<>();

	//每日领取的卡类型
	private ArrayList<Integer> dayRewardType = Lists.newArrayList();
	//key:充值id   value:充值次数
	private ConcurrentHashMap<Integer, Long> rechargeIdMap = new ConcurrentHashMap<>(); 
	
	private List<RechargeLogWarn> rechargeWarnList = Lists.newArrayList(); 
	
	
	//每日重置数据
	public void clearDayData() {
	}
	/**
	 * 充值月卡
	 * @param rechargeType
	 * @param day
	 */
	public void addCzYueka(int rechargeType,int day) {
		if(haveYueka(rechargeType)) {
			long endTime = yukaMap.get(rechargeType)+getYukaTime(day);
			this.yukaMap.put(rechargeType, endTime);
		}else{
			this.yukaMap.put(rechargeType, TimeUtils.getNowZero() + getYukaTime(day));
		}
		SetChanged();
	}
	
	/**
	 * 检查是否有月卡
	 * @param rechargeType
	 * @return
	 */
	public boolean haveYueka(int rechargeType) {
		if(yukaMap.containsKey(rechargeType)) {
			long endTime = yukaMap.get(rechargeType);
			if(endTime > System.currentTimeMillis()) {
				return true;
			}
			yukaMap.remove(rechargeType);
			SetChanged();
		}
		return false;
	}
	
	public long getYueKaDay(int rechargeType) {
		if(yukaMap.containsKey(rechargeType)) {
			long endTime = yukaMap.get(rechargeType);
			if(endTime < System.currentTimeMillis()) {
				return 0;
			}
			return DateUtil.betweenDay(this.yukaMap.get(rechargeType));
		}
		return 0;
	}
	
	//获取月卡的时效天数 -1毫秒,计算天数 
	private static long getYukaTime(int day) {
		return day*GameConstants.DAY-1;
	}
	public List<RechargeLogWarn> getRechargeWarnList() {
		return rechargeWarnList;
	}
	public void addRechargeWarn(RechargeLogWarn rechargeWarn) {
		rechargeWarnList.add(rechargeWarn);
		SetChanged();
	}
	public void clearRechargeWarn() {
		rechargeWarnList.clear();
		SetChanged();
	}
	
	public void reardKaType(int type) {
		this.dayRewardType.add(type);
		SetChanged();
	}
	
	public boolean isRewardKaType(int type) {
		return this.dayRewardType.contains(type);
	}
	
	public void addRechargeStatistics(int rechargeId) {
		this.rechargeIdMap.put(rechargeId,getRechargeStatistics(rechargeId)+1);
		SetChanged();
	}
	public long getRechargeStatistics(int rechargeId) {
		return this.rechargeIdMap.getOrDefault(rechargeId, 0L);
	}
	
	public void resetDay() {
		this.dayRewardType.clear();
		SetChanged();
	}

	public boolean haveSeasonVip(){
		return haveYueka(RechargeType.SeasonVipCard.getType());
	}

	public boolean haveSubscribeVip(){
		return haveYueka(RechargeType.SubscribeCard.getType());
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerRecharge", this);
	}
}



