package com.hm.action.http.merge;

import com.google.common.collect.Lists;
import com.hm.action.kf.KfObserverBiz;
import com.hm.action.kf.kfworldwar.KfWorldWarGameBiz;
import com.hm.action.kfseason.KFMergeSeasonBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.db.ActivityUtils;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardPutDescriptor;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.leaderboards.LeaderboardGetRank;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.IMergeRankAct;
import com.hm.servercontainer.activity.ActivityServerContainer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 活动和服处理
 * @Description: 活动和服处理
 * @author siyunlong  
 * @date 2020年1月3日 下午12:12:16 
 * @version V1.0
 */
@Slf4j
@Biz
public class ActivityMergeServerBiz {

	@Resource
	private KfWorldWarGameBiz kfWorldWarGameBiz;
	@Resource
	private KfObserverBiz kfObserverBiz;
	@Resource
	private CommValueConfig commValueConfig;
	

	public void doActivityMegerServer(int mainServerId,List<Integer> newServerIds) {
		try {
			log.error("=================活动和服:"+mainServerId);
			log.error("=================活动和服:"+GSONUtils.ToJSONString(newServerIds));
			//处理赛季信息
			KFMergeSeasonBiz.doMergeSeason(mainServerId, newServerIds);

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			doMergeRankForAct(mainServerId, newServerIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			kfWorldWarGameBiz.doKfWorldWar(mainServerId, newServerIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			doHBRank(mainServerId,newServerIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void doMergeRankForAct(int mainServerId,List<Integer> newServerIds) {
		for (AbstractActivity activity : ActivityServerContainer.of(mainServerId).getActivityList()) {
			try {
				if((activity instanceof IMergeRankAct) && activity.isOpen()) {
					log.error("合并排行榜:"+activity.getType());
					Set<String> rankList = ((IMergeRankAct)activity).getMergeRankNames();
					for (String rankName : rankList) {
						RankRedisUtils.mergeServerRank(mainServerId, newServerIds, rankName);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void doActivityRank(RankType rankType,int mainServerId,int srcServerId) {
		doActivityRank(rankType.getRankName(), mainServerId, srcServerId);
	}

	public static void doActivityRank(String rankName,int mainServerId,int srcServerId) {
		RankRedisUtils.mergeServerRank(mainServerId, Lists.newArrayList(srcServerId), rankName);
	}

	
	public AbstractActivity getAbstractActivityFromDB(int serverId,String activityId) {
		MongodDB mongo = MongoUtils.getGameMongodDB(serverId);
		return mongo.get(activityId, AbstractActivity.class,ActivityUtils.TableName);
	}
	
	public static List<LeaderboardInfo> getDelRank(int serverId,String rankName) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank("Del_Zslggame"+serverId, rankName);
		String result = HdLeaderboardsService.getInstance().getRanks(getRankDescriptor, 0, Integer.MAX_VALUE);
		return HdLeaderboardsService.getInstance().parseList(result);
	}
	public static List<LeaderboardInfo> getSubRank(int serverId,String rankName) {
		return HdLeaderboardsService.getInstance().getGameRank(serverId, rankName, 1,Integer.MAX_VALUE);
	}
	public static void addLeaderInfo(int serverId,List<LeaderboardInfo> rankList,String rankName) {
		for (LeaderboardInfo leaderboardInfo : rankList) {
			HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(serverId,leaderboardInfo.getId());
			putDescriptor.AddScore(rankName, leaderboardInfo.getScore());
			HdLeaderboardsService.getInstance().PutScoresForAdd(putDescriptor);
		}
	}

	/**
	 * 合服时处理排行合并
	 * @param mainServerId
	 * @param newServerIds
	 */
	public static void doHBRank(int mainServerId,List<Integer> newServerIds) {
		log.error(mainServerId+"合并排行->");
		RankRedisUtils.mergeServerRank(mainServerId, newServerIds, RankType.OverallWarRank.getRankName());
	}
}
