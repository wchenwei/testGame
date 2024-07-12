package com.hm.action.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.cityworld.biz.WorldCityBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarServerTeamUtils;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.loginmsg.LoginMsgBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.sys.SysFacade;
import com.hm.action.troop.biz.TroopFightBiz;
import com.hm.action.worldbuild.WorldBuildBiz;
import com.hm.container.PlayerContainer;
import com.hm.db.GmPlayerUtils;
import com.hm.db.PlayerUtils;
import com.hm.enums.ActivityType;
import com.hm.enums.LeaveOnlineType;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.main.CreateServerFactory;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.kfactivity.AbstractKfActivity;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.activity.kfactivity.KfKingsCanyonActivity;
import com.hm.model.activity.kfactivity.KfScuffleActivity;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.ClonePlayerUtils;
import com.hm.model.player.Player;
import com.hm.mongoqueue.MongoQueue;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.mission.MissionRecord;
import com.hm.servercontainer.world.WorldItemContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.util.HFUtils;
import com.hm.util.ItemUtils;
import com.hm.util.PubFunc;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Slf4j
@Service("serverope.do")
public class ServerOpeAction {
	@Resource
	private WorldCityBiz worldCityBiz;
	@Resource
    private LoginBiz loginBiz;
	@Resource
	private SysFacade sysFacade;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MailBiz mailBiz;

	@Resource
	private GuildTechBiz guildTechBiz;
	
	@Resource
	private WorldBuildBiz worldBuildBiz;
	
	/**
	 * 开启指定服务器
	 * @param session
	 */
	public void createServer(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			if(CreateServerFactory.createServer(serverId)) {
				session.Write("1");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	/**
	 * 加载所有新服
	 * @param session
	 */
	public void loadNewServer(HttpSession session) {
		try {
			CreateServerFactory.loadNewServer();
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	/**
	 * 关闭服务器
	 * @param session
	 */
	public void stopServer(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams().get("serverId"));
			//把所有玩家踢下线
			for (Player player : PlayerContainer.getOnlinePlayersByServerId(serverId)) {
				sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
			}
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
			CreateServerFactory.stopServer(serverId);
			log.error("==========game server is closed is"+serverId+"========");
			log.error(serverId+"==============服务器正常关闭==============");
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void clearServerWorld(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			worldCityBiz.clearWorldCity(serverId);
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void clearAllServerWorld(HttpSession session) {
		try {
			GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
				worldCityBiz.clearWorldCity(serverId);
			});
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}

	/**
	 * 同步整个服用户到redis
	 *
	 * @param session
	 */
	public void syncPlayerToRedis(HttpSession session) {
		try {
			for (Integer serverId : GameServerManager.getInstance().getServerIdList()) {
				PlayerUtils.syncPlayerToRedis(serverId);
			}
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void resetGuildTech(HttpSession session) {
		try {
			for (int serverId : GameServerManager.getInstance().getServerIdList()) {
				for (Guild guild : GuildContainer.of(serverId).getAllGuild()) {
					guildTechBiz.resetTech(guild);
					guild.saveDB();
				}
			}
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	/**
	 * 踢出玩家在线玩家
	 * @param session
	 */
	public void tickServerPlayer(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams().get("serverId"));
			//把所有玩家踢下线
			for (Player player : PlayerContainer.getOnlinePlayersByServerId(serverId)) {
				sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
			}
			log.error("==========tickServerPlayer"+serverId+"========");
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}

	/**
	 * 用户邮件读取
     * 合服时用
	 *
	 * @param session
	 */
	public void readPlayerMail(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			MongoTemplate mongoTemplate = MongoUtils.getServerMongoTemplate(serverId);
			if (mongoTemplate == null) {
				session.Write("0");
				return;
			}
			// 最后一次登录时间时间小于60天
			Date day60 = new Date(DateUtil.getProDays(new Date(), 60));
			Document filter = new Document("playerBaseInfo.lastLoginDate", new Document("$gt", day60));
			MongoCollection<Document> collection = mongoTemplate.getCollection("player");
			for (Document document : collection.find(filter)) {
				try {
					Player player = mongoTemplate.getConverter().read(Player.class, document);
					if (player == null) {
						continue;
					}
					Player cachePlayer = PlayerUtils.getPlayerFromOnlineOrCache(player.getId());
					if(cachePlayer != null) {
						player = cachePlayer;
					}
					List<Items> rewardList = mailBiz.readAllMail(player);
					if (rewardList.isEmpty()) {
						continue;
					}
					//此处不再记录收入日志，上一层已经在合并奖励前记录过
					itemBiz.addItem(player, rewardList, null);
					log.error(player.getId()+"hefu mail:"+ItemUtils.itemListToString(rewardList));
					player.sendUserUpdateMsgAndNowDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			session.Write("1");
			return;
		} catch (Exception e) {
			session.Write("0");
		}
		session.Write("0");
	}
	
	public void clearKfExpedetionData(HttpSession session) {
		try {
			for (int serverId : GameServerManager.getInstance().getServerIdList()) {
				KfExpeditionActivity activity = (KfExpeditionActivity)ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KfExpeditionActivity);
				if(activity != null) {
					if (!activity.isOpen()) {
						activity.setOpenType(2);
					}
					activity.resetData();
					activity.saveDB();
					
					SpringUtil.getBean(ActivityBiz.class).broadPlayerActivityUpdate(serverId);
				}
			}
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void clearWorldBuild(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			worldBuildBiz.checkWorldBuildOpen(serverId);
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void copyServerPlayer(HttpSession session) {
		try {
			int fromId = Integer.parseInt(session.getParams("fromId"));
			int toId = Integer.parseInt(session.getParams("toId"));
			if(ClonePlayerUtils.copyPlayer(fromId, toId)) {
				session.Write("1");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void clearMissionRecord(HttpSession session) {
		int[] ids = {7501,7502,7503,7504,7505,7601,7602,7603,7604,7605,7701,7702,7703,7704,7705,7801,7802,7803,7804,7805,7901,7902,7903,7904,7905,8001,8002,8003,8004,8005,8101,8102,8103,8104,8105,8201,8202,8203,8204,8205,8301,8302,8303,8304,8305,8401,8402,8403,8404,8405,8501,8502,8503,8504,8505,8601,8602,8603,8604,8605,8701,8702,8703,8704,8705,8801,8802,8803,8804,8805,8901,8902,8903,8904,8905,9001,9002,9003,9004,9005,9101,9102,9103,9104,9105,9201,9202,9203,9204,9205,9301,9302,9303,9304,9305,9401,9402,9403,9404,9405,9501,9502,9503,9504,9505,9601,9602,9603,9604,9605,9701,9702,9703,9704,9705,9801,9802,9803,9804,9805,9901,9902,9903,9904,9905,10001,10002,10003,10004,10005,10101,10102,10103,10104,10105,10201,10202,10203,10204,10205,10301,10302,10303,10304,10305,10401,10402,10403,10404,10405,10501,10502,10503,10504,10505,10601,10602,10603,10604,10605,10701,10702,10703,10704,10705};
		List<String> delIds = Lists.newArrayList();
		for (int id : ids) {
			delIds.add(id+"");
		}
		for (WorldItemContainer worldItemContainer : WorldServerContainer.getServerMap().getAllContainer()) {
			try {
				int serverId = worldItemContainer.getServerId();
				System.err.println(serverId+"=========================================");
				MongodDB mongo = MongoUtils.getMongodDB(serverId);
				Query query = new Query(Criteria.where("_id").in(delIds));
				List<MissionRecord> delList = mongo.query(query,MissionRecord.class,"missionrecord");
				if(CollUtil.isNotEmpty(delList)) {
					for (MissionRecord missionRecord : delList) {
						System.err.println(missionRecord.getId());
					}
					mongo.getMongoTemplate().remove(query, "missionrecord");
					worldItemContainer.initMissionRecord();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		session.Write("suc");
		return;
	}
	
	public void checkGameServerIdList(HttpSession session) {
		List<Integer> serverIds = Lists.newArrayList(GameServerManager.getInstance().getServerIdList());
		List<Integer> newServerIds = MongoUtils.getDbIdList();
		Collections.sort(serverIds);
		Collections.sort(newServerIds);
		String str1 = GSONUtils.ToJSONString(serverIds);
		String str2 = GSONUtils.ToJSONString(newServerIds);
		if(StrUtil.equals(str1, str2)) {
			session.Write("suc");
			return;
		}
		session.Write(str1+"="+str2);
		return;
	}

	public void reloadHFMark(HttpSession session) {
		try {
			HFUtils.reloadMark();
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	//http://192.168.1.96:8096/?action=serverope.do&m=changeMongoQueue&SaveDBSecondInterval=5
	public void changeMongoQueue(HttpSession session) {
		try {
			Map<String, String> keyMap = session.getParams();
			if(keyMap.containsKey("SaveDBSecondInterval")) {
				MongoQueue.SaveDBSecondInterval = PubFunc.parseInt(session.getParams("SaveDBSecondInterval"));
			}
			if(keyMap.containsKey("MaxWaitQueueSize")) {
				MongoQueue.MaxWaitQueueSize = PubFunc.parseInt(session.getParams("MaxWaitQueueSize"));
			}
			if(keyMap.containsKey("ShowLog")) {
				MongoQueue.ShowLog = StrUtil.equals(session.getParams("ShowLog"), "1");
			}
			if(keyMap.containsKey("isClose")) {
				MongoQueue.isClose = StrUtil.equals(session.getParams("isClose"), "1");
			}
			if(keyMap.containsKey("checkSecondIntervalTask")) {
				MongoQueue.checkSecondIntervalTask = StrUtil.equals(session.getParams("checkSecondIntervalTask"), "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SaveDBSecondInterval:"+MongoQueue.SaveDBSecondInterval+",");
		sb.append("MaxWaitQueueSize:"+MongoQueue.MaxWaitQueueSize+",");
		sb.append("ShowLog:"+MongoQueue.ShowLog+",");
		sb.append("isClose:"+MongoQueue.isClose+",");
		sb.append("waitQueueSize:"+MongoQueue.waitQueueSize.get()+",");
		sb.append("curQueueSize:"+MongoQueue.writeQueue.size()+",");
		sb.append("checkSecondIntervalTask:"+MongoQueue.checkSecondIntervalTask+",");
		session.Write(sb.toString());
		return;
	}
	public void changeCityCheck(HttpSession session) {
		TroopFightBiz.isCheckSameCampClose = StrUtil.equals(session.getParams("type"), "1");
		session.Write(TroopFightBiz.isCheckSameCampClose+"");
		return;
	}
	public void clearKfWz(HttpSession session) {
		for (ActivityItemContainer activityItemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
			KfKingsCanyonActivity canyonActivity = (KfKingsCanyonActivity)activityItemContainer.getAbstractActivity(ActivityType.KFKingsCanyon);
			if(canyonActivity != null) {
				canyonActivity.setFailType(0);
//				canyonActivity.clearData();
				canyonActivity.setServerUrl(null);
				canyonActivity.saveDB();
				canyonActivity.doCheckHourActivity();
			}
		}
		session.Write("suc");
		return;
	}
	
	public void clearKfSucffle(HttpSession session) {
		for (ActivityItemContainer activityItemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
			KfScuffleActivity activity = (KfScuffleActivity)activityItemContainer.getAbstractActivity(ActivityType.KFScuffle);
			if(activity != null) {
				activity.setFailType(0);
//				canyonActivity.clearData();
				activity.setServerUrl(null);
				activity.saveDB();
				activity.doCheckHourActivity();
			}
		}
		session.Write("suc");
		return;
	}

	public void reloadKFAct(HttpSession session) {
        KfWorldWarServerTeamUtils.reloadTeamMap();

		for (ActivityItemContainer activityItemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
			for(AbstractActivity act:activityItemContainer.getActivityList()) {
				if(act instanceof AbstractKfActivity) {
					act.doCheckHourActivity();
				}
			}
		}
		session.Write("suc");
		return;
	}


    public void clearAllLevelEvent(HttpSession session) {
        int serverId = Integer.parseInt(session.getParams("serverId"));
        List<Long> playerIds = PlayerUtils.getPlayerIdByCreateServerId(serverId, serverId);
        for (long playerId : playerIds) {
            Player p = PlayerUtils.getPlayer(playerId);
            p.playerEvent().clearAll();
            p.sendUserUpdateMsg();
        }
        session.Write("suc");
    }

	public void addGm(HttpSession session) {
		int id = Integer.parseInt(session.getParams("id"));
		GmPlayerUtils.addGm(id);
		session.Write("suc");
	}


	public void reloadLoginMsg(HttpSession session) {
		try {
			LoginMsgBiz.init();
			session.Write("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
	}
}
