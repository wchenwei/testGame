package com.hm.action.server;

import cn.hutool.core.date.DateUtil;
import com.hm.libcore.annotation.Biz;
import com.hm.leaderboards.DayRankRewardBiz;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.util.HFUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;

@Slf4j
@Biz
public class ServerHanderBiz implements IObserver{
	@Resource
	private DayRankRewardBiz dayRankRewardBiz;


	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this,0);
	}
	
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case HourEvent:
				doServerLv();
				checkGameServerId();
				doSendServerRank();
				break;
		}
	}
	
	/**
	 * 每天1点计算服务器等级
	 */
	public void doServerLv() {
		if(DateUtil.thisHour(true) != 1) {
			return;
		}
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			try {
				log.error(serverId+"每日:服务器计算等级开始");
				//计算服务器等级
				HFUtils.calServerLv(serverId);
				log.error(serverId+"每日:服务器计算等级结束");
			} catch (Exception e) {
				log.error(serverId+"每日:服务器计算等级异常!!!!!",e);
			}
		});	
	}

	public void doSendServerRank() {
		if(DateUtil.thisHour(true) != 2) {
			return;
		}
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			try {
				log.error(serverId+"每日:服务器排行奖励开始");
				//排行每日处理
				dayRankRewardBiz.doLeaderboardDayReward(serverId);
				log.error(serverId+"每日:服务器排行奖励结束");
			} catch (Exception e) {
				log.error(serverId+"每日:服务器排行奖励异常!!!!!", e);
			}
		});
	}
	
	public void checkGameServerId() {
		GameServerManager manager =	GameServerManager.getInstance();
		boolean isInit = false;
		for(int serverId:manager.getServerIdList()) {
			ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
			if(serverData == null) {
				isInit = true;
			}
		}
		for (int serverId : manager.getServerMap().keySet()) {
			if(!manager.isDbServer(manager.getDbServerId(serverId))) {
				isInit = true;
			}
		}
		if(isInit) {
			GameServerManager.getInstance().init();
		}
	}
	
}
