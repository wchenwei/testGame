package com.hm.model.activity.kfactivity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.enums.ActivityType;
import com.hm.message.ServerMsgType;
import com.hm.model.player.BasePlayer;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import java.util.List;

/**
 * @Description: 跨服远征战
 * @author siyunlong  
 * @date 2019年4月16日 下午4:14:31 
 * @version V1.0
 */
public class KfScoreActivity extends AbstractKfActivity{
	//对战的3个服务器
	private List<Integer> serverIds = Lists.newArrayList();
	
	public KfScoreActivity() {
		super(ActivityType.KFScoreWar);
	}
	
	@Override
	public boolean isCloseForPlayer(BasePlayer player) {
		return CollUtil.isEmpty(serverIds);
	}

	@Override
	public boolean checkCanAdd() {
		return true;
	}

	@Override
	public void doCheckHourActivity() {
		synchronized (this) {
			if(CollUtil.isEmpty(serverIds) || StrUtil.isEmpty(getServerUrl())) {
				KfScoreServerGroup scoreServerGroup = KfScoreServerGroup.findFitKfScoreServerGroup(getServerId());
				if(scoreServerGroup != null) {
					this.serverIds = scoreServerGroup.getServerIds();
					setServerUrl(scoreServerGroup.getUrl());
					saveDB();
					
					ActivityBiz activityBiz = SpringUtil.getBean(ActivityBiz.class);
					activityBiz.broadPlayerActivityUpdate(this);
					
					ObserverRouter.getInstance().notifyObservers(ObservableEnum.KFActOpen, null, getServerId());
				}
			}
		}
	}

	@Override
	public ServerMsgType getServerMsgType() {
		return ServerMsgType.KFScore;
	}

	
}
