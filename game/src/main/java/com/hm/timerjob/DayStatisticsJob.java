package com.hm.timerjob;

import cn.hutool.core.date.DateUtil;
import com.hm.action.cmq.CmqBiz;
import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DayStatisticsJob {
	private final static int CAMPTOPNUM = 20;
	@Resource
	private CmqBiz cmqBiz;

	//每天23点59执行统计
	@Scheduled(cron="0 59 3 * * ?") 
	public void doStatistics() {
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> doStatistics(serverId));
	}

	
	private void doStatistics(Integer serverId) {
		try {
			//查询本服玩家剩余金砖量
			List<Player> players = PlayerUtils.getStatistic(serverId,Criteria.where("playerBaseInfo.lastLoginDate").gte(DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7))));
			long sysGolds = players.stream().mapToLong(t -> t.playerCurrency().get(CurrencyKind.SysGold)).sum();
			long golds = players.stream().mapToLong(t -> t.playerCurrency().get(CurrencyKind.Gold)).sum();
			//石油和钞票统计平均数量
			long oil = players.stream().mapToLong(t -> t.playerCurrency().get(CurrencyKind.Oil)).sum()/Math.max(1, players.size());
			long cash = players.stream().mapToLong(t -> t.playerCurrency().get(CurrencyKind.Cash)).sum()/Math.max(1, players.size());
			ObserverRouter.getInstance().notifyObservers(ObservableEnum.GoldDaySurplus, null,serverId,sysGolds,golds,oil,cash,Math.max(1, players.size()));
		} catch (Exception e) {
			log.error("查询本服玩家剩余金砖量",e);
		}
	}
	
}
