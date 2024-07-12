package com.hm.model.activity;

import com.hm.enums.ActivityType;
import com.hm.model.player.BasePlayer;

/**
 * @Description: 永久活动->解锁副本后解锁活动
 * @author siyunlong  
 * @date 2019年2月22日 下午12:15:54 
 * @version V1.0
 */
public abstract class FbUnlockActivity extends PlayerUnlockActivity{
	
	public FbUnlockActivity(ActivityType type) {
		super(type);
	}
	
	public abstract int getUnlockFbId();
	/**
	 * 判断是否解锁此活动
	 * @param player
	 * @return
	 */
	@Override
	public boolean isCanUnlockActivity(BasePlayer player) {
		return player.playerMission().getFbId() >= getUnlockFbId();
	}
}
