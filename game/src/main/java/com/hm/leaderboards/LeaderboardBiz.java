package com.hm.leaderboards;

import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.activity.ActivityEffectBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.RankConfig;
import com.hm.config.excel.RankReward;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.RankTemplate;
import com.hm.enums.BattleType;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.RankType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.util.PubFunc;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 排行处理器
 * @author siyunlong  
 * @date 2018年4月11日 下午2:15:42 
 * @version V1.0
 */
@Slf4j
@Biz
public class LeaderboardBiz implements IObserver{
	@Resource
	private RankConfig rankConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private ActivityEffectBiz activityEffectBiz;
	@Resource
	private ActivityBiz activityBiz;

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.MaxCombatChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddHonor, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ClearnceMission, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TroopCombatChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
	}
	
	
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case MaxCombatChange:
				HdLeaderboardsService.getInstance().updatePlayerInfo(player);
				break;
			case AddHonor:
				doHonorAdd(player, (long)argv[1]);
				break;
			case ClearnceMission:
				updateMissionRank(player, (int)argv[0], (int)argv[1], (int)argv[3]);
				break;
			case TroopCombatChange:
				HdLeaderboardsService.getInstance().updatePlayerRankOverride(player, RankType.TroopCombat, player.getPlayerDynamicData().getTroopCombat());
				break;
		}
	}

	public void doHonorAdd(Player player,long add) {
		HdLeaderboardsService.getInstance().updatePlayerRankForAdd(player, RankType.TotalHonor, add);
	}
	
	public void doLeaderboardDayReset(int serverId) {
		//清空排行
		RankRedisUtils.renameRankList(serverId, RankType.getDayResetRanks());
//		//查看阵容荣誉排行是否重置
//		ServerCampData serverCampData = ServerDataManager.getIntance().getServerData(serverId)
//				.getServerCampData();
//		if(serverCampData.isCalHonorRank()) {
//			for (int camp = CampType.DG.getType(); camp <= CampType.GH.getType(); camp++) {
//				RankRedisUtils.renameRank(serverId, RankType.CampHonor.getRankName()+camp);
//			}
//		}
	}
	public void doRankReward(int serverId,RankType rankType) {
		try {
			doRedisRankReward(serverId, rankType, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doRedisRankReward(int serverId,RankType rankType,boolean isToday) {
		RankReward rankReward = rankConfig.getRankReward(rankType,serverId);
		if(rankReward == null) {
			log.error("处理排行奖励出错每日奖励:"+rankType.getDesc());
			return;
		}
		String rankName = rankType.getRankName();
		if(!isToday && rankType.isDayReset()) rankName = rankName +":"+RankRedisUtils.createMark();
		System.err.println(serverId+"排行奖励:"+rankName);
		List<Tuple> topRanks = RankRedisUtils.getRankList(serverId, rankName, 1, rankReward.getMaxRank());
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getRankRewardMail(rankType));
		if(mailTemplate == null) {
			log.error("处理排行奖励出错每日奖励邮件发放:"+rankType.getDesc());
			return;
		}
		for (int i = 0; i < topRanks.size(); i++) {
			Tuple leaderboardInfo = topRanks.get(i);
			int rank = i +1;
			RankTemplate rankTemplate = rankReward.getRankTemplate(rank);
			if(rankTemplate != null) {
				long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
				if(playerId<=0 
						//判断是否是此机器的玩家,用于跨服排行发放奖励
						|| !GameServerManager.getInstance().isServerMachinePlayer(playerId)){
					continue;
				}
				List<Items> itemList = doActivityAdd(rankType, rankTemplate.getRewardList(), playerId);
				mailBiz.sendSysMail(serverId, playerId, mailTemplate, itemList, LanguageVo.createStr(rank));
			}
		}
	}
	
	/**
	 * doRankReward:(大于等于最小分数的时候，才发奖励). <br/>  
	 * @author zxj  
	 * @param serverId
	 * @param rankType
	 * @param minScore  最小分数
	 *
	 */
	public void doRankReward(int serverId,RankType rankType, int minScore) {
		int serverLv = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics().getServerLv();
		doRankReward(serverId, serverLv, rankType, minScore);
	}
	public void doRankReward(int serverId,int serverLv,RankType rankType, int minScore) {
		RankReward rankReward = rankConfig.getRankRewardByServerLv(rankType,serverLv);
		if(rankReward == null) {
			log.error("处理排行奖励出错每日奖励:"+rankType.getDesc());
			return;
		}
		List<Tuple> topRanks = RankRedisUtils.getRankList(serverId, rankType.getRankName(), 1, rankReward.getMaxRank());
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getRankRewardMail(rankType));
		if(mailTemplate == null) {
			log.error("处理排行奖励出错每日奖励邮件发放:"+rankType.getDesc());
			return;
		}
		for (int i = 0; i < topRanks.size(); i++) {
			Tuple leaderboardInfo = topRanks.get(i);
			int rank = i +1;
			RankTemplate rankTemplate = rankReward.getRankTemplate(rank);
			if(rankTemplate != null) {
				long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
				if(playerId<=0 
						|| leaderboardInfo.getScore()<minScore
						|| !GameServerManager.getInstance().isServerMachinePlayer(playerId)){
					continue;
				}
				List<Items> itemList = doActivityAdd(rankType, rankTemplate.getRewardList(), playerId);

				mailBiz.sendSysMail(serverId, playerId, mailTemplate, itemList, LanguageVo.createStr(rank));
			}
		}
	}

	public void doRankReward(RankType rankType, int serverId, String rankName, int minScore) {
		RankReward rankReward = rankConfig.getRankRewardByServerLv(rankType, 1);
		if (rankReward == null) {
			log.error(rankName + "处理排行奖励出错每日奖励:" + rankType.getDesc());
			return;
		}
		List<Tuple> topRanks = RankRedisUtils.getRankList(serverId, rankName, 1, rankReward.getMaxRank());
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getRankRewardMail(rankType));
		if (mailTemplate == null) {
			log.error("处理排行奖励出错每日奖励邮件发放:" + rankType.getDesc());
			return;
		}
		for (int i = 0; i < topRanks.size(); i++) {
			Tuple leaderboardInfo = topRanks.get(i);
			int rank = i + 1;
			RankTemplate rankTemplate = rankReward.getRankTemplate(rank);
			if (rankTemplate != null) {
				long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
				if (playerId <= 0
						|| leaderboardInfo.getScore() < minScore
						|| !GameServerManager.getInstance().isServerMachinePlayer(playerId)) {
					continue;
				}
				int trueServerId = GameServerManager.getInstance().getDbServerId(serverId);
				mailBiz.sendSysMail(serverId, playerId, mailTemplate, rankTemplate.getRewardList(), LanguageVo.createStr(rank));

			}
		}
	}

	/**
	 * 发放活动奖励
	 * @param serverId 服务器id
	 * @param serverLv 服务器等级
	 * @param activity 活动
	 * @param rankType 排行榜
	 * @param minScore 大于等于最小分数的时候，才发奖励
	 */
	public void doActivityRankReward(int serverId, int serverLv, AbstractActivity activity, RankType rankType, int minScore) {
		RankReward rankReward = rankConfig.getRankRewardByServerLv(rankType,serverLv);
		if(rankReward == null) {
			log.error("处理活动排行奖励出错每日奖励:"+rankType.getDesc());
			return;
		}
		List<Tuple> topRanks = RankRedisUtils.getRankList(serverId, activity.getActivityRankName(rankType), 1, rankReward.getMaxRank());
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getRankRewardMail(rankType));
		if(mailTemplate == null) {
			log.error("处理活动排行奖励出错每日奖励邮件发放:"+rankType.getDesc());
			return;
		}
		for (int i = 0; i < topRanks.size(); i++) {
			Tuple leaderboardInfo = topRanks.get(i);
			int rank = i +1;
			RankTemplate rankTemplate = rankReward.getRankTemplate(rank);
			if(rankTemplate != null) {
				long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
				if(playerId<=0
						|| leaderboardInfo.getScore()<minScore
						|| !GameServerManager.getInstance().isServerMachinePlayer(playerId)){
					continue;
				}
				List<Items> itemList = doActivityAdd(rankType, rankTemplate.getRewardList(), playerId);
				mailBiz.sendSysMail(serverId, playerId, mailTemplate, itemList, LanguageVo.createStr(rank));

			}
		}
	}
	
	public List<Items> doActivityAdd(RankType rankType,List<Items> itemList,long playerId) {
		return itemList;
	}
	
	private void updateMissionRank(Player player,int battleType,int missionId,int result) {
		if(result == 0) {
			return;
		}
		RankType rankType = getMissionRankType(battleType);
		if(rankType == null) {
			return;
		}
		double value = DateUtil.parseTimeDouble(missionId);//根据再次封装
		HdLeaderboardsService.getInstance().updatePlayerRankOverride(player, rankType, value);
	}
	
	private RankType getMissionRankType(int battleType) {
		if(battleType == BattleType.MainBattle.getType()) {
			return RankType.PlayerMainBattle;
		}
		if(battleType == BattleType.TowerBattle.getType()) {
			return RankType.TowerRank;
		}
		return null;
	}
	/**
	 * 获取最终排行分数
	 * @param score:主排序分数,withScore 副排序分数,beSub:副排序分数的被减数,scale：精确到小数点几位 order: 副排序分数 的排序方式(true:副排序分数正序排列(如战力)，false:副排序方式倒叙排列(如时间))
	 */
	public static double getFinalScore(double score,double withScore,double beSub,int scale,boolean order){
		//分数+.秒数
		double finalScore = order?score+withScore/beSub:score+(beSub-withScore)/beSub;
		BigDecimal big = new BigDecimal(finalScore);
		finalScore = big.setScale(scale, BigDecimal.ROUND_HALF_DOWN).doubleValue(); 
		return finalScore;
	}
	public static double toCombatScore(long score,long combat) {
        return getFinalScore(score, combat / 10000, 1000000, 6, true);
	}
	
	public static void main(String[] args) {
        System.out.println(toCombatScore(1, Integer.MAX_VALUE));
	}
}
