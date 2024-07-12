package com.hm.action.kf.kfkingcanyon;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.action.activity.ActivityBiz;
import com.hm.enums.ActivityType;
import com.hm.model.activity.kfactivity.KfKingsCanyonActivity;
import com.hm.servercontainer.activity.ActivityServerContainer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;

@Slf4j
@Biz
public class KfKingCanyonBiz {

	@Resource
    private ActivityBiz activityBiz;
	

	public void doKfServerFail(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			log.error("收到跨服王者峡谷消息:"+serverId);
			KfKingsCanyonActivity kingsCanyonActivity = (KfKingsCanyonActivity)ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KFKingsCanyon);
			if(kingsCanyonActivity == null || !kingsCanyonActivity.isOpen()) {
				return;//活动未开放
			}
			kingsCanyonActivity.loadFailType();
			kingsCanyonActivity.saveDB();
			activityBiz.broadPlayerActivityUpdate(kingsCanyonActivity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
