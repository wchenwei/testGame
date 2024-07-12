package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;

/**
 * @Description: 玩家世界建筑
 * @author siyunlong  
 * @date 2019年10月16日 上午12:07:25 
 * @version V1.0
 */
public class PlayerWorldBuildData extends PlayerDataContext{
	//每个阵营的采集积分
	private long[] campScores = new long[3];
	//每日阵营是否领取奖励
	private int[] rewards = new int[3];
	private boolean todayReward;//今日是否领取基础奖励
	
	public void addCampScore(int campId,long add) {
		this.campScores[campId-1] += add;
		SetChanged();
	}
	public long getCampScore(int campId) {
		return this.campScores[campId-1];
	}
	
	public boolean isCanReward(int campId) {
		return this.rewards[campId-1] == 0;
	}
	
	public void doCampReward(int campId) {
		this.rewards[campId-1] = 1;
		SetChanged();
	}
	
	
	public boolean isTodayReward() {
		return todayReward;
	}
	public void setTodayReward(boolean todayReward) {
		this.todayReward = todayReward;
		SetChanged();
	}
	
	public void dayReset() {
		this.campScores = new long[3];
		this.rewards = new int[3];
		this.todayReward = false;
		SetChanged();
	}
	
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerWorldBuildData", this);
	}
}
