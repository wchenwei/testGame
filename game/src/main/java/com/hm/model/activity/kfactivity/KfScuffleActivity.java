package com.hm.model.activity.kfactivity;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.config.GameConstants;
import com.hm.enums.ActivityType;
import com.hm.model.kf.KfScuffleServer;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import lombok.Getter;

import java.util.List;

/**
 * @Description: 跨服乱斗
 * @author xjt  
 * @date 2020年11月10日15:50:08 
 * @version V1.0
 */
@Getter
public class KfScuffleActivity extends AbstractKfActivity{
	//所有报名玩家id
	private List<Long> joinIds = Lists.newArrayList();
	private int failType;//1-掉队的  2-人数不足
	private List<Integer> fightServerIds = Lists.newArrayList();//对战服务器id
	private int openDay;
	
	public KfScuffleActivity() {
		super(ActivityType.KFScuffle);
	}
	
	@Override
	public void doCreateActivity() {
		ServerData serverData =  ServerDataManager.getIntance().getServerData(getServerId());
		if(serverData != null) {
			this.openDay = serverData.getServerStatistics().getOpenDay();
		}
	}
	
	@Override
	public boolean isOpen() {
		return this.openDay > 21 && super.isOpen();
	}


	@Override
	public boolean checkCanAdd() {
		return true;
	}
	

	public boolean isSignupTime() {
		return System.currentTimeMillis() < getEndTime() - GameConstants.DAY;
	}
	
	public void sign(long playerId) {
		this.joinIds.add(playerId);
	}
	public void loadKfServer() {
		KfScuffleServer KfScuffleServer = new KfScuffleServer(getServerId(), joinIds);
		KfScuffleServer.setOpenDay(openDay);
		KfScuffleServer.saveDB();
	}
	
	public void loadFailType() {
		this.failType = 1;
	}
	public void setFailType(int failType) {
		this.failType = failType;
	}


	public void clearData() {
		this.failType = 0;
		this.joinIds.clear();
		this.fightServerIds.clear();
	}

	@Override
	public void doCheckHourActivity() {
		if(isSignupTime()) {
			return;
		}
		synchronized (this) {
			if(StrUtil.isEmpty(getServerUrl())) {
				KfScuffleServerGroup scoreServerGroup = BaseKfServerGroup.findFitKfServerGroup(getServerId(), KfScuffleServerGroup.class);
				if(scoreServerGroup != null) {
					this.fightServerIds = scoreServerGroup.getServerIds();
					setServerUrl(scoreServerGroup.getUrl());
					saveDB();
					ActivityBiz activityBiz = SpringUtil.getBean(ActivityBiz.class);
					activityBiz.broadPlayerActivityUpdate(this);
					
					ObserverRouter.getInstance().notifyObservers(ObservableEnum.KFActOpen, null, getServerId());
				}
			}
		}
	}

}
