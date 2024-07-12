package com.hm.timerjob;

import com.hm.action.activity.ActivityBiz;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildMemberBiz;
import com.hm.action.guild.task.GuildTaskBiz;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.serverData.ServerPowerBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.container.PlayerContainer;
import com.hm.db.ActivityUtils;
import com.hm.enums.ServerFunctionType;
import com.hm.leaderboards.DayRankRewardBiz;
import com.hm.leaderboards.LeaderboardBiz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.log.PlayerLogBiz;
import com.hm.message.MessageComm;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerStatistics;
import com.hm.observer.ObservableEnum;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.servercontainer.guild.GuildContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class DayResetJob {
	public static boolean isSyncRankReward = true;
	

	@Resource
	private LoginBiz loginBiz;

	@Resource
	private GuildBiz guildBiz;
	@Resource
	private GuildMemberBiz guildMemberBiz;
	@Resource
	private LeaderboardBiz leaderboardBiz;

	@Resource
	private ActivityBiz activityBiz; 
	@Resource
	private CommValueConfig commValueConfig; 
	@Resource
	private CmqBiz cmqBiz;
	@Resource
	private ServerPowerBiz serverPowerBiz;
	@Resource
	private GuildTaskBiz guildTaskBiz;
	@Resource
	private DayRankRewardBiz dayRankRewardBiz;
	@Resource
	private PlayerLogBiz playerLogBiz;
	

	/**
	 * 发出小时事件之前执行
	 */
	public void doZeroTimerPreHour() {
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> doZeroTimerPreHour(serverId));
	}
	public void doZeroTimerPreHour(int serverId) {
		try {
			log.error(serverId+"每日:服务器天数开始");
	        ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics();
	        serverStatistics.addOpenDay();
	        serverStatistics.save();
			log.error(serverId+"每日:服务器天数结束");
		} catch (Exception e) {
			log.error(serverId+"每日:服务器天数异常!!!!!",e);
		}
	}
	
	//每天0点执行
//	@Scheduled(cron="1 0 0 * * ?")  不在用定时器执行
	public void doZeroTimer() {
		log.info("=========doZeroTimer==重置每天任务，每天0点执行=========");
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> doServerZeroBefore(serverId));
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> doServerZeroAfter(serverId));
		log.info("=========doZeroTimer==重置每天任务，每天0点执行完毕=========");
	}
	
	//充值玩家每日信息
	private void doResetPlayer(int serverId) {
		JsonMsg dateChangeMsg = JsonMsg.create(MessageComm.S2C_DateChange);
		Date now = new Date();
		for (Player player : PlayerContainer.getOnlinePlayersByServerId(serverId)) {
			try {
				player.notifyObservers(ObservableEnum.PlayerLoginZero);
			} catch (Exception e) {
				log.error("发送玩家统计异常", e);
			}
			try {
				if(loginBiz.resetPlayerByZero(player)) {
					//记录登录日志
					playerLogBiz.logLogin(player);
					//重置最后一次登录时间
					player.playerBaseInfo().setLastLoginDate(now);
					player.sendUserUpdateMsg();
				}
				player.sendMsg(dateChangeMsg);//发送日期变更消息
			} catch (Exception e) {
				log.error("充值玩家每日信息", e);
			}
		}
	}
	/**
	 * 玩家数据重置之前执行
	 * @param serverId
	 */
	private void doServerZeroBefore(int serverId) {

		try {
			log.error(serverId+"每周一0点:计算部落任务等级开始");
			 //计算阵营等级
			if (1 == DateUtil.getCsWeek()) {
				guildTaskBiz.doWeekReset(serverId);
			}
			log.error(serverId+"每周一0点:计算部落任务等级开始");
		} catch (Exception e) {
			log.error(serverId+"每周一0点:计算部落任务等级异常!!!!!");
		}
		try {
			log.error(serverId+"每日:服务器排行重置开始");
			//排行每日处理
			leaderboardBiz.doLeaderboardDayReset(serverId);
			log.error(serverId+"每日:服务器排行重置结束");
		} catch (Exception e) {
			log.error(serverId+"每日:服务器排行重置异常!!!!!", e);
		}
		try {
			log.error(serverId+"每日:重新计算服务器活动");
			activityBiz.checkActivityForServer(serverId);
			log.error(serverId+"每日:重新计算服务器活动");
		} catch (Exception e) {
			log.error(serverId+"每日:重新计算服务器活动出错!!!");
		}
		try {
			log.error(serverId+"每日:重新计算服务器每日标识");
			doServerMark(serverId);
			log.error(serverId+"每日:重新计算服务器每日标识");
		} catch (Exception e) {
			log.error(serverId+"每日:重新计算服务器每日标识出错!!!");
		}
		try {
			log.error(serverId+"每日:服务器处理部落开始");
			//处理部落
			doGuildClear(serverId);
			log.error(serverId+"每日:服务器处理部落结束");
		} catch (Exception e) {
			log.error(serverId+"每日:服务器处理部落异常!!!!!", e);
		}
		//处理玩家重置
		try {
			log.error(serverId+"每0点:玩家信息重置");
			doResetPlayer(serverId);
		} catch (Exception e) {
			log.error("doZeroTimer==重置每天任务", e);
		}
		try {
			//计算阵营等级
			if (1 == DateUtil.getCsWeek()) {
				ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
				if (serverData.getServerFunction().isServerUnlock(ServerFunctionType.EliminatePlay.getType())) {
					log.error(serverId + "每周一0点:重新生成服务器大军压境地图开始");
					serverData.getServerEliminate().resetWeek();
					serverData.save();
				}
			}
			log.error(serverId + "每周一0点:重新生成服务器大军压境地图结束");
		} catch (Exception e) {
			log.error(serverId + "每周一0点:重新生成服务器大军压境地图异常!!!!!");
		}
	}
	
	private void doServerPower(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		serverData.getServerPresidentPower().clearCenterCity();
		if(serverData.getServerPresidentPower().Changed()){
			serverData.save();
			serverPowerBiz.broadPowerChange(serverId);
		}
	}
	private void doServerMark(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		serverData.getServerStatistics().loadServerDayMark();
		serverData.getServerWorldBuildData().resetData();
	}

	
	/**
	 * 玩家数据重置之后执行
	 * @param serverId
	 */
	private void doServerZeroAfter(int serverId) {

		try {
			log.error(serverId+"每日:服务器活动处理开始");
			//活动处理
			doActivityDayReset(serverId); 
			log.error(serverId+"每日:服务器活动处理结束");
		} catch (Exception e) {
			log.error(serverId+"每日:服务器活动处理异常!!!!!", e);
		}
		try {
			log.error(serverId+"每日:服务器游戏服基本信息开始");
			//把游戏服基本信息发送到统计服
			statisServerMsg(serverId);
			log.error(serverId+"每日:服务器游戏服基本信息结束");
		} catch (Exception e) {
			log.error(serverId+"每日:服务器游戏服基本信息异常!!!!!", e);
		}

		try {
			log.error(serverId+"每日:服务器处理中心城");
			doServerPower(serverId);
			log.error(serverId+"每日:服务器处理中心城");
		} catch (Exception e) {
			log.error(serverId+"每日:服务器处理中心城出错");
		}
		//改到每天凌晨4点发放
//		try {
//			log.error(serverId+"每日:服务器排行奖励开始");
//			//排行每日处理
//			if(DayRankRewardBiz.isNewDayRankReward) {
//				dayRankRewardBiz.doLeaderboardDayReward(serverId);
//			}else{
//				leaderboardBiz.doLeaderboardDayReward(serverId);
//			}
//			log.error(serverId+"每日:服务器排行奖励结束");
//		} catch (Exception e) {
//			log.error(serverId+"每日:服务器排行奖励异常!!!!!", e);
//		}
	}

	//定时清理长时间不上线的玩家
	public void doGuildClear(int serverId) {
		GuildContainer.of(serverId).getDataMap().forEach((guildId,guild)->{
			try {
				guild.doDayReset();//每日重置
			} catch (Exception e) {
				log.error(serverId+"每日:重置阵营异常：" + guild.getId(), e);
			}
		});
	}

	public void doActivityDayReset(int serverId){
		for (AbstractActivity activity : ActivityServerContainer.of(serverId).getActivityList()) {
			try {
				if(activity.isOpen() && activity.doByZero()){
					ActivityUtils.saveOrUpdate(activity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		activityBiz.broadPlayerActivityUpdate(serverId);
	}
	//把游戏服基本信息发送到统计服
	private void statisServerMsg(int serverId) {
		cmqBiz.statisServerMsg(serverId);
	}

	
}





