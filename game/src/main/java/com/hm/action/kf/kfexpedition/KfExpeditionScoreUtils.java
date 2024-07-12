package com.hm.action.kf.kfexpedition;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.mission.biz.PlayerMissionBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.model.kf.KfExpeditionServer;
import com.hm.libcore.mongodb.MongoUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KfExpeditionScoreUtils {
	/**
	 * 前10的战力总和
	 * @param serverId
	 * @return
	 */
	public static long getTotalCombat(int serverId,int rank) {
		List<LeaderboardInfo> topRanks = HdLeaderboardsService.getInstance().getGameRank(serverId, RankType.Combat.getRankName(), 1, rank);
		if(topRanks.isEmpty()) {
			return 0;
		}
		return topRanks.stream().mapToLong(e -> (long)e.getScore()).sum();
	}
	
	public static void checkFirstScore(int serverId) {
		KfExpeditionServer kfServer = getKfExpeditionServer(serverId);
		if(kfServer == null) {
			kfServer = new KfExpeditionServer();
			kfServer.setId(serverId);
			kfServer.setServerId(serverId);
			kfServer.setScore(40);
			kfServer.saveDB();
		}
	}
	
	public static KfExpeditionServer getKfExpeditionServer(int serverId) {
		return MongoUtils.getLoginMongodDB().get(serverId, KfExpeditionServer.class);
	}
	
	public static List<KfExpeditionServer> getAllKfExpeditionServer() {
		return MongoUtils.getLoginMongodDB().queryAll(KfExpeditionServer.class);
	}
	
	public static void removeMergeServer(int serverId) {
		//找出积分最大的
		List<ServerTempInfo> allServerList = MongoUtils.getLoginMongodDB().queryAll(ServerTempInfo.class,"serverInfo");
		//找出被和服的服务器id
		List<ServerTempInfo> serverList = allServerList
				.stream().filter(e -> e.getServer_id() != serverId && e.getDb_id() == serverId)
				.collect(Collectors.toList());
		//找出当前所有和服到本服服务器
		List<Integer> memerList = allServerList.stream().filter(e -> e.getServer_id() == serverId || e.getDb_id() == serverId)
				.map(e -> e.getServer_id())
				.collect(Collectors.toList());
		//找出最大积分服务器
        List<KfExpeditionServer> otherList = getAllKfExpeditionServer().stream().filter(e -> memerList.contains(e.getId()))
                .collect(Collectors.toList());
        int maxScore = otherList.stream().mapToInt(e -> e.getScore()).max().orElse(0);
		for (ServerTempInfo serverTempInfo : serverList) {
			MongoUtils.getLoginMongodDB().delete(serverTempInfo.getServer_id(), KfExpeditionServer.class);
		}
		//取当前最大的积分
		KfExpeditionServer mainServer = getKfExpeditionServer(serverId);
        if (mainServer == null && CollUtil.isNotEmpty(otherList)) {
			//被和的服开启跨服远征,本服没有开启,要把本服开启
			KfExpeditionBiz expeditionBiz = SpringUtil.getBean(KfExpeditionBiz.class);
			expeditionBiz.calOldServerKf(serverId);
			return;
		}
		if(mainServer != null && mainServer.getScore() < maxScore) {
			mainServer.setScore(maxScore);
			mainServer.saveDB();
		}
	}
	
	public static void main(String[] args) {
		removeMergeServer(6);
	}
	
}
