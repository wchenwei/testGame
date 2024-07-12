package com.hm.model.activity;

import cn.hutool.core.util.StrUtil;
import com.hm.enums.ActivityType;
import com.hm.model.player.BasePlayer;

public abstract class PlayerActivityValue {
	private String activityId;//所属的activity
	private PlayerAcitivtyOpenTime openTime;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	/**
	 * 如果活动是跟随玩家开启,则设置玩家的开启和结束时间
	 * @param openTime
	 */
	public void setPlayerAcitivtyOpenTime(PlayerAcitivtyOpenTime openTime) {
		this.openTime = openTime;
	}
	public PlayerAcitivtyOpenTime getOpenTime() {
		return openTime;
	}
	/**
	 * 检查是否关闭活动处理
	 */
	public boolean doActivityClose(BasePlayer player) {
		
		return false;
	}
	
	/**
	 * 处理重复开启活动的上期数据
	 */
	public void clearRepeatValue() {}
	
	public abstract boolean checkCanReward(BasePlayer player,int id);
	public abstract void setRewardState(BasePlayer player,int id);
	
	/**
	 * 是否是同一个活动
	 * @param activity
	 * @return
	 */
	public boolean isSameActivity(AbstractActivity activity) {
		if(activity == null) {
			return false;
		}
		if(ActivityType.isForeverType(activity.getType())) {
			return true;
		}
		return StrUtil.equals(activity.getId(), this.activityId);
	}
}
	
