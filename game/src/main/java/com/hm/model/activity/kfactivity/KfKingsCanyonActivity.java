package com.hm.model.activity.kfactivity;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.config.GameConstants;
import com.hm.enums.ActivityType;
import com.hm.model.kf.KfKingCanyonServer;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 跨服王者峡谷
 * @author siyunlong  
 * @date 2020年3月31日 下午12:04:39 
 * @version V1.0
 */
public class KfKingsCanyonActivity extends AbstractKfActivity{
	private transient KfSignPlayer[] playerIds = new KfSignPlayer[10];
	//所有报名玩家id
	private List<Long> joinIds = Lists.newArrayList();
	private int failType;//1-掉队的  2-人数不足
	private List<Integer> fightServerIds = Lists.newArrayList();//对战服务器id
	private int openDay;
	
	public KfKingsCanyonActivity() {
		super(ActivityType.KFKingsCanyon);
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
	
	public KfSignPlayer getPlayerById(long playerId) {
		return Arrays.stream(this.playerIds).filter(e -> e != null && e.getId() == playerId).findFirst().orElse(null);
	}
	public KfSignPlayer getPlayerByIndex(int index) {
		return this.playerIds[index];
	}
	public void setPlayerIdByIndex(int index,KfSignPlayer signPlayer) {
		this.playerIds[index] = signPlayer;
		this.joinIds = Arrays.stream(this.playerIds).filter(e -> e != null).map(e -> e.getId()).collect(Collectors.toList());
	}
	public KfSignPlayer[] getPlayerIds() {
		return playerIds;
	}
	public void loadKfServer() {
		Set<Long> ids = Arrays.stream(this.playerIds).filter(Objects::nonNull).map(e -> e.getId()).collect(Collectors.toSet());
		KfKingCanyonServer kingCanyonServer = new KfKingCanyonServer(getServerId(), ids);
		kingCanyonServer.saveDB();
	}
	
	public void loadFailType() {
		this.failType = Arrays.stream(this.playerIds).anyMatch(e -> e == null)?2:1;
	}
	public void setFailType(int failType) {
		this.failType = failType;
	}
	
	public void clearData() {
		this.failType = 0;
		this.playerIds = new KfSignPlayer[10];
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
				KfKingServerGroup scoreServerGroup = BaseKfServerGroup.findFitKfServerGroup(getServerId(), KfKingServerGroup.class);
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
