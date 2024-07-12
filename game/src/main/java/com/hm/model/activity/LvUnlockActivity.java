package com.hm.model.activity;

import com.hm.enums.ActivityType;
import com.hm.model.player.BasePlayer;

/**
 * @Description: 永久活动->达到等级后解锁活动
 * @author siyunlong  
 * @date 2019年2月22日 下午12:16:22 
 * @version V1.0
 */
public abstract class LvUnlockActivity extends PlayerUnlockActivity{
	private int unlockLv;
	
	public LvUnlockActivity(ActivityType type,int unlockLv) {
		super(type);
		this.unlockLv = unlockLv;
	}
	/**
	 * 判断是否解锁此活动
	 * @param player
	 * @return
	 */
	@Override
	public boolean isCanUnlockActivity(BasePlayer player) {
		return player.playerLevel().getLv()>=this.getUnlockLv();
	}
	public int getUnlockLv() {
		return unlockLv;
	}
	
	
}
