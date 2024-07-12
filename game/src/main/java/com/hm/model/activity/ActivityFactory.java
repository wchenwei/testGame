package com.hm.model.activity;

import com.hm.config.GameConstants;
import com.hm.enums.ActivityType;

public class ActivityFactory {
	public static AbstractActivity createAbstractActivity(ActivityType activityType,long startTime,long endTime) {
		AbstractActivity activity = activityType.getActivityTemplate();
		if(!activityType.isForeverType()) {
			activity.setActivityTime(startTime, endTime);
		}
		return activity;
	}
	
	public static AbstractActivity createAbstractActivity(ActivityType activityType,int day) {
		AbstractActivity activity = activityType.getActivityTemplate();
		if(!activityType.isForeverType()) {
			long now = System.currentTimeMillis();
			activity.setActivityTime(now, now+day*GameConstants.DAY);
		}
		return activity;
	}
}
