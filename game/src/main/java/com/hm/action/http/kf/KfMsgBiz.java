package com.hm.action.http.kf;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildWorldBiz;
import com.hm.action.http.MergeContants;
import com.hm.action.http.biz.BroadServerBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.kf.KfMineUtils;
import com.hm.action.kf.kfexpedition.KfExpeditionServerResult;
import com.hm.action.kf.kfkingcanyon.KfKingCanyonBiz;
import com.hm.action.kf.kfscuffle.KfScuffleBiz;
import com.hm.action.kf.kfworldwar.KfWorldWarGameBiz;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarConf;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSnumTime;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSumUtils;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.push.util.PushUtil;
import com.hm.action.worldbuild.WorldBuildBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.RankConfig;
import com.hm.config.excel.RankReward;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.RankTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Biz
public class KfMsgBiz {
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
    private MailBiz mailBiz;
	@Resource
	private RankConfig rankConfig;
	@Resource
	private CommValueConfig commValueConfig;
	
	@Resource
	private ActivityBiz activityBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private GuildWorldBiz guildWorldBiz;
	@Resource
	private BroadServerBiz broadServerBiz;
	@Resource
	private WorldBuildBiz worldBuildBiz;
	@Resource
	private KfKingCanyonBiz kfKingCanyonBiz;
	@Resource
	private KfScuffleBiz kfScuffleBiz;
	@Resource
	private KfWorldWarGameBiz kfWorldWarGameBiz;

	
	

	public boolean doMsgEvent(HttpSession session,String msgId) {
        log.error("doMsgEvent:" + msgId);

		if(StrUtil.equals(msgId, "sendServerKfManorOcc")) {
			doServerManorOcc(session);
		}else if(StrUtil.equals(msgId, "sendKfManorReward")) {
			sendKfManorReward(session);
		}else if(StrUtil.equals(msgId, "sendMineRob")) {
			sendMineRob(session);
		}else if(StrUtil.equals(msgId, "sendRankReward")) {
			sendRankReward(session);
		}else if(StrUtil.equals(msgId, "sendMineRankReward")) {
			sendMineRankReward(session);
		}else if(StrUtil.equals(msgId, "sendKfMineChange")) {
			sendKfMineChange(session);
		}else if(StrUtil.equals(msgId, "sendGuildOccupyCityReward")) {
			sendGuildOccupyCityReward(session);
		}else if(StrUtil.equals(msgId, "sendKfExpeditionServerResult")) {
			sendKfExpeditionServerResult(session);
		}else if(StrUtil.equals(msgId, "sendKFObserver")) {
			sendKFObserver(session);
		}else if(StrUtil.equals(msgId, "sendKfBackOilServerResult")) {
			sendKfBackOilServerResult(session);
		}else if(StrUtil.equals(msgId, "broadAllServerMsg")) {
			broadServerBiz.doBroadAllServerMsg(session);
		}else if(StrUtil.equals(msgId, "sendGameKfKingFail")) {
			kfKingCanyonBiz.doKfServerFail(session);
		}else if(StrUtil.equals(msgId, "sendGameKfScuffleFail")) {
			kfScuffleBiz.doKfServerFail(session);
		}else if(StrUtil.equals(msgId,"sendKFMail")){
			mailBiz.sendKfMail(session);
		} else if (StrUtil.equals(msgId, "sendWordWarSnumEnd")) {
			sendKfLevelChangeKing(session);
		}
		return true;
	}

	public void sendKfLevelChangeKing(HttpSession session) {
		try {
			List<String> playerIds = GSONUtils.FromJSONString(session.getParams("ids"), new TypeToken<List<String>>() {
			}.getType());
			long endTime = DateUtil.offsetMonth(DateUtil.beginOfDay(new Date()), KfWorldWarConf.SnumMonth).getTime();
			KfWorldWarSnumTime nextWarSnumTime = KfWorldWarSnumTime.getKfWorldWarSnumTime(KfWorldWarSumUtils.getSnumId() + 1);
			if (nextWarSnumTime != null) {
				endTime = nextWarSnumTime.getCalTime();
			}
			for (String playerId : playerIds) {
				try {
					log.error(playerId + "跨服世界大战结束奖励");
					kfWorldWarGameBiz.doWorldWarPlayerTitle(playerId, endTime);
				} catch (Exception e) {
					e.printStackTrace();
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
	
	public void doServerManorOcc(HttpSession session) {
		try {
			String serverName = URLDecoder.decode(session.getParams("serverName"), "utf-8");
			String manorName = URLDecoder.decode(session.getParams("manorName"), "utf-8");
			int serverId = Integer.parseInt(session.getParams("serverId"));
			ObserverRouter.getInstance().notifyObservers(ObservableEnum.KfManorOcc, null, serverName,manorName,serverId);
		} catch (Exception e) {
			log.error("占领广播出错:",e);
		}
	}
	
	public void sendKfManorReward(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			int rank = Integer.parseInt(session.getParams("rank"));
			doServerReward(serverId, rank);
		} catch (Exception e) {
			log.error("sendKfManorReward:",e);
		}
	}
	
	public void doServerReward(int serverId,int rank) {
		RankReward rankReward = rankConfig.getRankReward(RankType.KfManorScore);
		RankTemplate rankTemplate = rankReward.getRankTemplate(rank);
		if(rankTemplate == null) {
			log.error("error跨服领地战奖励serverId:"+serverId+" rank:"+rank);
			return;
		}
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.KfManorReward);
		if(mailTemplate == null) {
			log.error("error跨服领地战奖励邮件不存在!");
			return;
		}
		List<Items> allItemList = rankTemplate.getRewardList();
		double maxRate = MathUtils.div(commValueConfig.getCommValue(CommonValueType.KFManorMaxRate), 100);
		List<LeaderboardInfo> topRanks = HdLeaderboardsService.getInstance().getGameRank(serverId, RankType.KfManorScore.getRankName(), 1, 3000);
		long totalScore = topRanks.stream().mapToLong(e -> (long)e.getScore()).sum();
		for (LeaderboardInfo leaderboardInfo : topRanks) {
			try {
				Player player = PlayerUtils.getPlayer(leaderboardInfo.getIntId());
				if(player != null) {
					double trueRate = MathUtils.div(leaderboardInfo.getScore(), totalScore,2);
					double rate = Math.min(maxRate, trueRate);
					String showRate = ((int)(trueRate*100))+"%";
					List<Items> itemList = ItemUtils.calItemRateReward(allItemList, rate);
					mailBiz.sendSysMail(player, mailTemplate.getMail_id(), itemList,LanguageVo.createStr(rank,(int)leaderboardInfo.getScore(),showRate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void sendMineRob(HttpSession session) {
		try {
			if(MergeContants.isMergeRunning) {
				return;
			}
			long playerId = Long.parseLong(session.getParams("playerId"));
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null) {
				PushUtil.sendMessage(player, PushType.MineRob);
			}
		} catch (Exception e) {
			log.error("sendKfManorReward:",e);
		}
	}
	
	public void sendRankReward(HttpSession session) {
		try {
			RankType rankType = RankType.getTypeByIndex(Integer.parseInt(session.getParams("rankType")));
			int serverId = Integer.parseInt(session.getParams("serverId"));
			List<KfPlayerRank> valueList = GSONUtils.FromJSONString(session.getParams("valueList"), new TypeToken<List<KfPlayerRank>>(){}.getType());
			
			RankReward rankReward = rankConfig.getRankReward(rankType);
			if(rankReward == null) {
				log.error("跨服排行奖励出错每日奖励:"+rankType.getDesc());
				return;
			}
			MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getRankRewardMail(rankType));
			if(mailTemplate == null) {
				log.error("跨服处理排行奖励出错每日奖励邮件发放:"+rankType.getDesc());
				return;
			}
			for (KfPlayerRank playerValue : valueList) {
				RankTemplate rankTemplate = rankReward.getRankTemplate(playerValue.getRank());
				this.addPlayerHeadRankReward(playerValue.getId(), rankTemplate);
				if(rankTemplate != null) {
					List<Items> itemList = rankTemplate.getRewardList();
					mailBiz.sendSysMail(serverId, playerValue.getId(), mailTemplate, itemList, LanguageVo.createStr(playerValue.getRank()));
				}
			}
		} catch (Exception e) {
			log.error("sendKfManorReward:",e);
		}
	}

	/**
	 * 给玩家添加头像
	 */
	private void addPlayerHeadRankReward(long playerId,RankTemplate rankTemplate) {
		if(rankTemplate == null){
			return;
		}
		Items headItem = rankTemplate.getHeadItem();
		if(headItem == null) {
			return;
		}
		Player player = PlayerUtils.getPlayer(playerId);
		if(player == null) {
			return;
		}
		itemBiz.addItem(player, headItem, LogType.RankReward);
	}
	
	public void sendMineRankReward(HttpSession session) {
		try {
			RankType rankType = RankType.getTypeByIndex(Integer.parseInt(session.getParams("rankType")));
			int serverId = Integer.parseInt(session.getParams("serverId"));
			List<KfMinePlayerRank> valueList = GSONUtils.FromJSONString(session.getParams("valueList"), new TypeToken<List<KfMinePlayerRank>>(){}.getType());
			
			RankReward rankReward = rankConfig.getRankReward(rankType);
			if(rankReward == null) {
				log.error("跨服排行奖励出错每日奖励:"+rankType.getDesc());
				return;
			}
			MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getRankRewardMail(rankType));
			if(mailTemplate == null) {
				log.error("跨服处理排行奖励出错每日奖励邮件发放:"+rankType.getDesc());
				return;
			}
			for (KfMinePlayerRank playerValue : valueList) {
				try {
					RankTemplate rankTemplate = rankReward.getRankTemplate(playerValue.getRank());
					if(rankTemplate != null) {
						List<Items> itemList = Lists.newArrayList(rankTemplate.getRewardList());
						long oil = playerValue.getValues()[0];
						long cash = playerValue.getValues()[1];
						if(oil > 0) {
							itemList.add(new Items(PlayerAssetEnum.Oil.getTypeId(), oil, ItemType.CURRENCY));
						}
						if(cash > 0) {
							itemList.add(new Items(PlayerAssetEnum.Cash.getTypeId(), cash, ItemType.CURRENCY));
						}
						mailBiz.sendSysMail(serverId, playerValue.getId(),mailTemplate, itemList,LanguageVo.createStr(playerValue.getRank()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error("sendKfManorReward:",e);
		}
	}

	public void sendKfMineChange(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null) {
				return;
			}
			boolean haveMine = KfMineUtils.playerHaveMine(playerId);
			if(player.isOnline()) {
				KfMineUtils.sendPlayerMineChange(player,haveMine);
			}
			if(!haveMine) {//通知
				player.notifyObservers(ObservableEnum.KFMineLose);
			}
		} catch (Exception e) {
			log.error("sendKfMineChange:",e);
		}
	}
	
	public void sendGuildOccupyCityReward(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			int cityId = Integer.parseInt(session.getParams("cityId"));
			List<Items> itemList = ItemUtils.str2ItemList(session.getParams("rewardItems"), ",", ":");
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null && itemList.size() > 0) {
				Guild guild = guildBiz.getGuild(player);
				if(guild != null) {
					guildWorldBiz.doMailOccupyCityReward(guild, cityId, itemList.get(0),DateUtil.today());
				}
			}
		} catch (Exception e) {
			log.error("sendKfMineChange:",e);
		}
	}
	
	public void sendKfExpeditionServerResult(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			KfExpeditionServerResult serverResult = GSONUtils.FromJSONString(session.getParams("serverResult"), KfExpeditionServerResult.class);
			sendKfExpeditionServerResult(serverResult);
		} catch (Exception e) {
			log.error("sendKfExpeditionServerResult:",e);
		}
	}
	public void sendKfExpeditionServerResult(KfExpeditionServerResult serverResult) {
		try {
			int serverId = serverResult.getServerId();
			KfExpeditionActivity expeditionActivity = (KfExpeditionActivity)ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KfExpeditionActivity);
			boolean activityIsOpen = expeditionActivity != null && expeditionActivity.isOpen();
			if(!activityIsOpen) {
				log.error("跨服远征奖励发送异常!活动未开放,"+serverId);
				return;
			}
			expeditionActivity.doFailServer(serverResult.getFailServerId());
			expeditionActivity.setWinServerId(serverResult.getWinServerId());
			expeditionActivity.saveDB();
			activityBiz.broadPlayerActivityUpdate(expeditionActivity);

			MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.KfExpeditionOil);
			if(mailTemplate == null) {
				log.error("跨服远征反返石油出错每日奖励邮件发放:");
				return;
			}
			for (Map.Entry<Integer, String> entry : serverResult.getPlayerMap().entrySet()) {
				try {
					long playerId = entry.getKey();
					List<Items> itemList = ItemUtils.str2ItemList(entry.getValue(), ",", ":");
					if(itemList.size() > 0) {
						mailBiz.sendSysMail(serverId, playerId,mailTemplate, itemList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(serverId == serverResult.getWinServerId()) {
				ObserverRouter.getInstance().notifyObservers(ObservableEnum.KfExpeditionOccWin, null,serverId);
			}else{
				ObserverRouter.getInstance().notifyObservers(ObservableEnum.KfExpeditionOccFail, null,serverId);
			}
			worldBuildBiz.checkWorldBuildOpen(expeditionActivity);

		} catch (Exception e) {
			log.error("sendKfExpeditionServerResult:",e);
		}
	}

	
	public void sendKfBackOilServerResult(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			KfExpeditionServerResult serverResult = GSONUtils.FromJSONString(session.getParams("serverResult"), KfExpeditionServerResult.class);
			int mailType = Integer.parseInt(session.getParams("mailType"));
			sendKfBackOilServerResult(serverResult);
		} catch (Exception e) {
			log.error("sendKfManorServerResult:",e);
		}
	}
	public void sendKfBackOilServerResult(KfExpeditionServerResult serverResult) {
		MailTemplate mailTemplate = mailConfig.getMailTemplate(serverResult.getMailType());
		if(mailTemplate == null) {
			log.error("sendKfManorServerResult跨服远征反返石油出错每日奖励邮件发放:");
			return;
		}
		for (Map.Entry<Integer, String> entry : serverResult.getPlayerMap().entrySet()) {
			try {
				long playerId = entry.getKey();
				List<Items> itemList = ItemUtils.str2DefaultItemList(entry.getValue());
				if(itemList.size() > 0) {
					mailBiz.sendSysMail(serverResult.getServerId(), playerId, mailTemplate, itemList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void sendKFObserver(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			int observerId = Integer.parseInt(session.getParams("observerId"));
			String parmas = session.getParams("args");
			ObservableEnum observableEnum = ObservableEnum.getObservableEnum(observerId);
			if(observableEnum == null) {
				log.error("sendKFObserver:id 找不到"+observerId);
				return;
			}
			if(StrUtil.isNotEmpty(parmas)) {
				List<String> valueList = GSONUtils.FromJSONString(parmas, new TypeToken<List<String>>(){}.getType());
				ObserverRouter.getInstance().notifyObservers(observableEnum, null,valueList.toArray());
			}else{
				ObserverRouter.getInstance().notifyObservers(observableEnum, null);
			}
		} catch (Exception e) {
			log.error("sendKFObserver:",e);
		}
	}

}
