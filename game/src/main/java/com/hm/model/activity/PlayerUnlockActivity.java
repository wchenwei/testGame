package com.hm.model.activity;

import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.templaextra.ActivityMainTemplate;
import com.hm.enums.ActivityType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import org.springframework.data.annotation.Transient;

/**
 * @Description: 玩家达到一定条件才解锁的活动
 * @author siyunlong  
 * @date 2019年2月22日 下午2:41:50 
 * @version V1.0
 */
public abstract class PlayerUnlockActivity extends AbstractActivity{
	@Transient
	private transient ActUnlock actUnlock;

	@Override
	public void initServerLoad() {
		doCreateActivity();
	}

	@Override
	public void doCreateActivity() {
		ActivityConfig activityConfig = SpringUtil.getBean(ActivityConfig.class);
		ActivityMainTemplate template = activityConfig.getActivityMainTemplate(getType());
		this.actUnlock = ActUnlock.build(template.getUnlock());
	}


	public PlayerUnlockActivity(ActivityType type) {
		super(type);
	}
	
	/**
	 * 判断玩家是否开启此活动
	 * @param player
	 * @return
	 */
	public boolean isCanUnlockActivity(BasePlayer player) {
		return this.actUnlock == null || this.actUnlock.isUnlock(player);
	}
	/**
	 * 处理玩家解锁活动
	 * @param player
	 */
	public abstract void doUnlockActivity(BasePlayer player);
	
	/**
	 * 是否可以重复解锁
	 */
	public boolean isCanRepeatUnlock(Player player) {
		return false;
	}
	
	@Override
	public boolean isCloseForPlayer(BasePlayer player) {
		if(!isCanUnlockActivity(player)) {
			return true;
		}
		return player.getPlayerActivity().isCloseActivity(getType());
	}
}
