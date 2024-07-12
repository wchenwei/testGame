package com.hm.timerjob;

import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.trade.biz.TradeBiz;
import com.hm.config.excel.MailConfig;
import com.hm.container.PlayerContainer;
import com.hm.leaderboards.LeaderboardBiz;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.guild.GuildContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class HourTimerJob {
	//全面战争开启星期
	public final static List<Integer> OpenWeeks = Lists.newArrayList(2,4,6);
	
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private ActivityBiz activityBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private TradeBiz tradeBiz;
	@Resource
	private DayResetJob dayResetJob;
	@Resource
	private LeaderboardBiz leaderboardBiz;
	

	//每个小时检查一下在线用户，更新在先用户的任务状态
	//不在线的用户，会再获取任务信息的时候，重置一下任务信息
	@Scheduled(cron="0 0 0/1 * * ?")  
	public void doCampTask() {
		if(!ServerStateCache.serverIsRun()) {
			return;
		}
		try {
			log.error("===========检查部落战开启=========");
			GuildWarUtils.checkHourForWar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			log.error("===========每小时在线检查=========");
			//如果当前时间，小于下次清楚时间，则表示要清楚在先用户的任务完成数量
			doOnlinePlayerHour();
		} catch (Exception e) {
			log.error("每小时在线",e);
		}
		try {
			log.error("===========每小时活动检查=========");
			//重新加载活动数据
			activityBiz.checkActivity();
		} catch (Exception e) {
			log.error("每小时活动",e);
		}
		//
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			try {
				doServerHour(serverId);
			} catch (Exception e) {
				log.error(serverId+"每小时服务器",e);
			}
		});
		
		//0点执行
		if(DateUtil.thisHour(true) == 0) {
			dayResetJob.doZeroTimerPreHour();
		}
		//抛出整点事件
		ObserverRouter.getInstance().notifyObservers(ObservableEnum.HourEvent, null, DateUtil.thisHour(true));
		//0点执行
		if(DateUtil.thisHour(true) == 0) {
			dayResetJob.doZeroTimer();
		}
		try {
			log.error("===========每小时部落检查=========");
			//每小时部落检查
			doGuildHour();
		} catch (Exception e) {
			log.error("每小时部落检查",e);
		}
	}
	
	private void doOnlinePlayerHour() {
		//如果当前时间，小于下次清楚时间，则表示要清楚在先用户的任务完成数量
		List<Player> playerList = PlayerContainer.getOnlinePlayers();
		for(Player player:playerList) {
			try {
				boolean isUpdate = false;
				//每小时增加征收次数去除
				/*if(tempPlayer.playerTreasury().checkFreeCount()){
					isUpdate = true;
				}*/
				if(player.playerShop().loginCheck()){
					isUpdate = true;
				}
				//每小时结算一次航运公司产出
				if (tradeBiz.checkout(player)) {
					isUpdate = true;
				}
				if(isUpdate) {
					player.sendUserUpdateMsg();
				}
			} catch (Exception e) {
				log.error(player.getId()+"每小时服务器检查出错!",e);
			}
		}
	}
	
	private void doServerHour(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		//重新加载信息
		serverData.getServerKfData().loadHourCheck();
		//重新加载跨服地址
		serverData.loadServerKfUrl();
	}
	
	//每小时统计一次部落的信息
//	@Scheduled(cron="0 0 0/1 * * ?")  
	public void doGuildHour() {
		log.error("============che每小时部落检查============");
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			try {
				GuildContainer.of(serverId).getDataMap().forEach((guildId,guild)->{
					guildBiz.checkLeader(guild);
					
					guildBiz.doHourJob(guild);
					if(guild.getGuildImpeach().Changed()) {
						guild.saveDB();
					}
				});
			} catch (Exception e) {
				log.error("每小时部落检查",e);
			}
		});
	}
	
	//每小时30分的时候发出
	@Scheduled(cron="0 0/1 * * * ?")  
	public void doMinuteTask() {
		ObserverRouter.getInstance().notifyObservers(ObservableEnum.MinuteEvent, null, DateUtil.thisMinute());
	}
	//每小时结算一次航运公司产出
//	@Scheduled(cron="0 0 0/1 * * ?")
//	public void doTradeCompany() {
//		List<Player> playerList = PlayerContainer.getOnlinePlayers();
//		for (Player player : playerList) {
//			if (tradeBiz.checkout(player)) {
//				player.playerTrade().SetChanged();
//				player.sendUserUpdateMsg();
//			}
//		}
//	}
	
}











