package com.hm.model.activity.threedays;

import com.hm.action.activity.biz.ActivityTaskBiz;
import com.hm.enums.ActivityType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.activity.PlayerAcitivtyOpenTime;
import com.hm.model.activity.PlayerUnlockActivity;
import com.hm.model.player.BasePlayer;

import java.util.Date;
/**
 * @Description: 三日活动-新7日活动
 * @author yanpeng
 * @date 2019年2月14日 下午2:23:32 
 * @version V1.0
 */	
public class ThreeDayActivity extends PlayerUnlockActivity {
	
	public ThreeDayActivity() {
		super(ActivityType.ThreeDay);
	}


	@Override
	public boolean isCloseForPlayer(BasePlayer player) {
		if (super.isCloseForPlayer(player)){
			return true;
		}
		PlayerThreeDayValue playerActivityValue = (PlayerThreeDayValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.ThreeDay);
		if(playerActivityValue.getOpenTime() == null) {
			return true;
		}
		return !playerActivityValue.getOpenTime().isOpen();
	}

	@Override
	public void doUnlockActivity(BasePlayer player) {
		PlayerThreeDayValue playerThreeDayValue = (PlayerThreeDayValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.ThreeDay);
		playerThreeDayValue.setPlayerAcitivtyOpenTime(new PlayerAcitivtyOpenTime(player, 0, new Date(),9));
		ActivityTaskBiz activityTaskBiz = SpringUtil.getBean(ActivityTaskBiz.class);
		activityTaskBiz.reloadActiviyTask(player, ActivityType.ThreeDay);
		player.getPlayerActivity().SetChanged();
	}


}
