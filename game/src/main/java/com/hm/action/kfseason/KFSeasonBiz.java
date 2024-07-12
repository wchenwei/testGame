package com.hm.action.kfseason;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.titile.TitleBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.KfConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.model.activity.ActivityFactory;
import com.hm.model.activity.kfSeasonShop.KfSeasonShopActivity;
import com.hm.model.activity.kfSeasonShop.KfSeasonShopValue;
import com.hm.model.activity.kfseason.KFSeasonActivity;
import com.hm.model.activity.kfseason.server.*;
import com.hm.model.item.ItemGroup;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.Title;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.PubFunc;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Biz
public class KFSeasonBiz implements IObserver{

	@Resource
    private ActivityBiz activityBiz;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private TitleBiz titleBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private KfConfig kfConfig;
	
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.KFActOpen, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case KFActOpen:
				doKFActOpen((int)argv[0]);
				break;
			case HourEvent:
				doHourEvent((int) argv[0]);
				break;
			case Recharge:
				doShop(player,argv);
				break;
		}
	}
	
	private void doShop(Player player, Object[] argv) {
		KfSeasonShopActivity activity = (KfSeasonShopActivity) ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFSeasonShop);
		if(activity==null||!activity.isOpen()) {
			return;
		}
		int rechargeId = (int)argv[3];
		if(!kfConfig.isShopGiftId(rechargeId)) {
			return;
		}
		//根据配置把rechatgeId转换为goodsId
		int goodsId = kfConfig.getGoodsId(activity.getServerLv(),activity.getVersion(),rechargeId);
		if(goodsId<0) {
			return;
		}
		KfSeasonShopValue value = (KfSeasonShopValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.KFSeasonShop);
		value.buy(player, goodsId);
		player.getPlayerActivity().SetChanged();
		
	}

	/**
	 * 检查跨服赛季是否开启
	 * @param serverId
	 */
	public void doKFActOpen(int serverId) {
		KFSeasonActivity activity = (KFSeasonActivity) ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KFSeason);
		if(activity != null && !activity.isOpen()) {
			//添加
			int groupId = ServerDataManager.getIntance().getServerData(serverId).getServerKefuData().getSeasonGroupId();
			if(groupId <= 0) {
				return;
			}
			KFSeasonServer seasonServer = new KFSeasonServer(serverId,groupId,KFSeasonUtil.getCurSeason().getId());
			seasonServer.saveDB();
			KfSeasonServerUtils.addKFSeasonServer(seasonServer);//加入排行
			
			KFSeason kFSeason = KFSeason.getKFSeasonTime();
			activity.loadNewSeason(kFSeason);
			activity.setOpen(true);
			activity.saveDB();
			activityBiz.broadPlayerActivityUpdate(activity);
		}
	}


	/**
	 * 根据服务器排名获取服务器奖励
	 * @param rank
	 * @param seasonId
	 * @return
	 */
	public List<Items> buildServerItems(int rank,int seasonId) {
		return kfConfig.getKfSeasonServerReward(seasonId, rank);
	}
	/**
	 * 根据玩家积分发放奖励
	 * @param score 玩家积分
	 * @param seasonId 赛季id
	 * @return
	 */
	public List<Items> buildPlayerScoreItems(int seasonId) {
		try {
			List<ItemGroup> seasonTopItemList = commValueConfig.getSeasonTopItemList();
			int size = seasonTopItemList.size();
			if(size > 0) {
				return seasonTopItemList.get(Math.max(seasonId-1, 0)%size).getItemList();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Lists.newArrayList();
	}
	
	/**
	 * 开启商店
	 * xx名之前（包括xx名）的服务器开启冠绝商店，xx = 向上取整（参数1 * 排行榜服务器数量  / 5）* 5
	 * @param serverId
	 * @param serverRank
	 * @param totalNum
	 */
	public void openSeasonShop(int serverId,int serverRank,int totalNum) {
		double rate = commValueConfig.getDoubleCommValue(CommonValueType.KFSeasonRate);
		int minRank = (int)(Math.ceil(rate*totalNum/5))*5;
		//  1 冠绝商店  2	力战商店
		int type = serverRank > minRank?2:1;
		KfSeasonShopActivity activity = (KfSeasonShopActivity)ActivityFactory.createAbstractActivity(ActivityType.KFSeasonShop,
                System.currentTimeMillis(), DateUtil.beginOfDay(new Date()).getTime()+2*GameConstants.DAY);
		activity.setVersion(type);
		activity.setServerId(serverId);
		ActivityServerContainer.of(serverId).addActivity(activity);
	}
	
	
	public static int getServerRank(List<KFSeasonServer> allList,int serverId) {
		for (int i = 0; i < allList.size(); i++) {
			if(allList.get(i).getId() == serverId) {
				return i+1;
			}
		}
		return -1;
	}
	
	/**
	 * 处理赛季结束
	 * @param activity
	 */
	public void doKfSeasonEnd(KFSeasonActivity activity) {
		int serverId = activity.getServerId();
		int runSeasonId = activity.getSeasonId();//正在进行的赛季id
		//服务器所在分组id
		int groupId = ServerDataManager.getIntance().getServerData(serverId).getServerKefuData().getSeasonGroupId();
		
		List<KFSeasonServer> serverRankList = KfSeasonServerUtils.getAllServerRank(serverId, runSeasonId, groupId);
		int serverRank = getServerRank(serverRankList, serverId);
		if(serverRank <= 0) {
			System.err.println(activity.getServerId()+"赛季不存在:"+serverRank);
			doCreateNewSeasonForError(activity);
			return;
		}
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.KFSeasonServerReward);
		MailTemplate scoreTemplate = mailConfig.getMailTemplate(MailConfigEnum.KFSeasonScoreReward);
		//个人最低积分奖励15000
		int minPlayerScore = commValueConfig.getCommValue(CommonValueType.KFSeasonPlayerScoreReward);
		List<Items> playerScoreRewards = buildPlayerScoreItems(runSeasonId);
		List<Items> severItemList = buildServerItems(serverRank, runSeasonId);
		
		Set<Long> serverPlayerIds = Sets.newHashSet();
		Set<Long> scorePlayerIds = Sets.newHashSet();
		//获取战功排行，给战功>0的玩家发放奖励
		String rankName = RankType.SeasionPlayerScore.getConfRankName()+runSeasonId;
		List<Tuple> topRanks = RankRedisUtils.getRankList(activity.getServerId(), rankName, 1, Integer.MAX_VALUE);
		for (int i = 0; i < topRanks.size(); i++) {
			Tuple leaderboardInfo = topRanks.get(i);
			int score = (int)leaderboardInfo.getScore();
			if(score > 0) {
				long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
				serverPlayerIds.add(playerId);
				if(score >= minPlayerScore) scorePlayerIds.add(playerId);
			}
		}
		System.err.println(serverId+"赛季服务器:"+GSONUtils.ToJSONString(serverPlayerIds));
		System.err.println(serverId+"赛季积分:"+GSONUtils.ToJSONString(playerScoreRewards));
		//发放服务器奖励
		mailBiz.sendSysMail(serverId, serverPlayerIds, mailTemplate, severItemList, LanguageVo.createStr(serverRank));
		//发放个人积分奖励
		mailBiz.sendSysMail(serverId, scorePlayerIds, scoreTemplate, playerScoreRewards);
		
		
		KFSeason nextSeason = KFSeason.getKFSeasonTime(runSeasonId+1);
		if(serverRank == 1 && topRanks.size() > 0) {
			KFSeason curSeason = KFSeason.getKFSeasonTime(runSeasonId);
			Tuple leaderboardInfo = topRanks.get(0);
			long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
			int score = (int)leaderboardInfo.getScore();
			SesaonGodWarPlayer winPlayer = new SesaonGodWarPlayer(playerId,score);
			System.err.println("赛季战神:"+GSONUtils.ToJSONString(winPlayer));
			
			KFSeasonTopLog topLog = new KFSeasonTopLog(curSeason.getId(), groupId, 
					KfSeasonServerUtils.getKFSeasonServer(runSeasonId, serverId), winPlayer);
			topLog.save();
			//给第一名发放战神称号
			doGodWarPlayerTitle(winPlayer.getPlayerId(),nextSeason);
		}
		KFSeasonUtil.removeSeasonCache(runSeasonId);
		//=================生成新赛季数据====================================
		KFSeasonServer seasonServer = new KFSeasonServer(serverId,groupId,nextSeason.getId());
		seasonServer.saveDB();
		
		activity.loadNewSeason(nextSeason);
		activity.saveDB();
		//开放商店
		openSeasonShop(serverId, serverRank, serverRankList.size());
		//通知所有在线玩家
		activityBiz.broadPlayerActivityUpdate(serverId);
	}

	public void doCreateNewSeasonForError(KFSeasonActivity activity) {
		int serverId = activity.getServerId();
		int runSeasonId = activity.getSeasonId();//正在进行的赛季id
		//服务器所在分组id
		KFSeason nextSeason = KFSeason.getKFSeasonTime(runSeasonId + 1);
		int groupId = ServerDataManager.getIntance().getServerData(serverId).getServerKefuData().getSeasonGroupId();
		KFSeasonServer seasonServer = new KFSeasonServer(serverId, groupId, nextSeason.getId());
		seasonServer.saveDB();

		activity.loadNewSeason(nextSeason);
		activity.saveDB();
		//开放商店
		openSeasonShop(serverId, 90, 100);
		//通知所有在线玩家
		activityBiz.broadPlayerActivityUpdate(serverId);
	}
	
	/**
	 * 发放战神称号
	 * @param playerId
	 */
	public void doGodWarPlayerTitle(long playerId,KFSeason nextSeason) {
		Player player = PlayerUtils.getPlayer(playerId);
		if(player != null) {
			//2个月
			Title title = new Title(PlayerTitleType.WarGod.getType());
			title.setEndTime(nextSeason.getEndTime());
			titleBiz.addTitle(player, title);

            MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.KFSeasonScoreTopTitle);
			if(mailTemplate != null) {
				mailBiz.sendSysMail(player, mailTemplate, null);
			}
		}
	}

	public void checkSeasonEnd() {
		KFSeason season = KFSeasonUtil.getCurSeason();
        if (System.currentTimeMillis() > season.getEndTime()) {
            for (ActivityItemContainer activityItemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
                try {
                    KFSeasonActivity activity = (KFSeasonActivity) activityItemContainer.getAbstractActivity(ActivityType.KFSeason);
                    if (activity.isOpen()) {
                        doKfSeasonEnd(activity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
			}
		}
		KFSeasonUtil.reloadSeasonId();
	}

	public void doHourEvent(int hour) {
		if (hour == 0) {
			System.out.println("检查赛季结算");
			checkSeasonEnd();
		}
		/**
		 * 1,合服是早上10点完成
		 * 2,跨服是晚上21点结束
		 * 3,跨服军演是晚上22点结束
		 */
		if(hour == 0 || hour == 10 || hour == 21 || hour == 22) {
			KfSeasonServerUtils.reloadKFSeasonServerFromDB();
		}
	}

}
