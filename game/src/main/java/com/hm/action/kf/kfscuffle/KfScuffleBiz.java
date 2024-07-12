package com.hm.action.kf.kfscuffle;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.action.activity.ActivityBiz;
import com.hm.enums.ActivityType;
import com.hm.model.activity.kfactivity.KfScuffleActivity;
import com.hm.servercontainer.activity.ActivityServerContainer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;

@Slf4j
@Biz
public class KfScuffleBiz {

	@Resource
    private ActivityBiz activityBiz;
	

	public void doKfServerFail(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			log.error("收到极地乱斗消息:"+serverId);
			KfScuffleActivity activity = (KfScuffleActivity)ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KFScuffle);
			if(activity == null || !activity.isOpen()) {
				return;//活动未开放
			}
			activity.loadFailType();
			activity.saveDB();
			activityBiz.broadPlayerActivityUpdate(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
