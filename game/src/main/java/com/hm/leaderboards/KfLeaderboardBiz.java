package com.hm.leaderboards;

import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.annotation.Biz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.RankConfig;
import com.hm.config.excel.RankReward;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.RankTemplate;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.RankType;
import com.hm.server.GameServerManager;
import com.hm.util.PubFunc;
import com.hm.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Biz
public class KfLeaderboardBiz {
	@Resource
	private RankConfig rankConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;

	

	/**
	 * 指定服务器等级发放奖励，不用服务器当前等级
	 *
	 * @param serverId
	 * @param rankType
	 * @param rankName
	 * @param trueServerId
	 * @param fixedSrvLv
	 */
	public void doKFRedisRankReward(int serverId,RankType rankType,String rankName,int trueServerId, int fixedSrvLv) {
		RankReward rankReward = rankConfig.getRankRewardByServerLv(rankType, fixedSrvLv);
		if(rankReward == null) {
			log.error("处理排行奖励出错每日奖励:"+rankType.getDesc());
			return;
		}
		System.err.println(serverId+"排行奖励:"+rankName);
		doReward(serverId, rankType, rankName, trueServerId, rankReward);
	}

	private void doReward(int serverId, RankType rankType, String rankName, int trueServerId, RankReward rankReward) {
		List<Tuple> topRanks = RankRedisUtils.getRankList(serverId, rankName, 1, rankReward.getMaxRank());
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getRankRewardMail(rankType));
		if(mailTemplate == null) {
			log.error("处理排行奖励出错每日奖励邮件发放:"+ rankType.getDesc());
			return;
		}
		for (int i = 0; i < topRanks.size(); i++) {
			Tuple leaderboardInfo = topRanks.get(i);
			int rank = i +1;
			RankTemplate rankTemplate = rankReward.getRankTemplate(rank);
			if(rankTemplate != null) {
				long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
				int playerServerId = GameServerManager.getInstance().getDbServerId(ServerUtils.getCreateServerId(playerId));
				if(playerServerId == trueServerId) {
					mailBiz.sendSysMail(ServerUtils.getServerId(playerId), playerId, mailTemplate, RankType.getRankRewardsByScore(rankType, rankTemplate.getRewardList(), leaderboardInfo.getScore())
						, LanguageVo.createStr(rank));
				}
			}
		}
	}

	public void doKFRedisRankReward(int serverId,RankType rankType,String rankName,int trueServerId) {
		RankReward rankReward = rankConfig.getRankReward(rankType,serverId);
		if(rankReward == null) {
			log.error("处理排行奖励出错每日奖励:"+rankType.getDesc());
			return;
		}
		System.err.println(serverId+"排行奖励:"+rankName);
		doReward(serverId, rankType, rankName, trueServerId, rankReward);
	}

	public void doKfRankReward(RankType rankType, String rankName, int minScore, int serverId) {
		RankReward rankReward = rankConfig.getRankRewardByServerLv(rankType, 1);
		if (rankReward == null) {
			log.error(rankName + "处理排行奖励出错每日奖励:" + rankType.getDesc());
			return;
		}
		List<Tuple> topRanks = RankRedisUtils.getRankList(0, rankName, 1, rankReward.getMaxRank());
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
				int playerServerId = GameServerManager.getInstance().getDbServerId(ServerUtils.getCreateServerId(playerId));
				if(serverId!=playerServerId) {
					continue;
				}
				int trueServerId = GameServerManager.getInstance().getDbServerId(ServerUtils.getCreateServerId(playerId));
				mailBiz.sendSysMail(trueServerId, playerId, mailTemplate, rankTemplate.getRewardList(),LanguageVo.createStr(rank));
			}
		}
	}
	
}
