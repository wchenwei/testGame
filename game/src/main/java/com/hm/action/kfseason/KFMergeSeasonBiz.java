package com.hm.action.kfseason;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.enums.ActivityType;
import com.hm.enums.RankType;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.activity.kfseason.server.KFSeasonServer;
import com.hm.model.activity.kfseason.server.KfSeasonServerUtils;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 和服对赛季处理
 * @author siyunlong  
 * @date 2020年12月11日 下午3:48:40 
 * @version V1.0
 */
@Biz
public class KFMergeSeasonBiz{

	@Resource
	private KFSeasonBiz seasonBiz;
	
	public static void doMergeSeason(int mainServerId,List<Integer> newServerIds) {
		//合并玩家战功排行
		RankRedisUtils.mergeServerRank(mainServerId, newServerIds, RankType.SeasionPlayerScore.getRankName());
		//合并服务器赛季积分
		List<Integer> allServerIds = Lists.newArrayList(mainServerId);
		allServerIds.addAll(newServerIds);
		
		List<KFSeasonServer> serverList = allServerIds.stream().map(e -> KfSeasonServerUtils.getCurSeasonKFSeasonServer(e))
				.filter(e -> e != null)
				.collect(Collectors.toList());
		KFSeasonServer maxServer = serverList.stream().max(Comparator.comparingInt(KFSeasonServer::getScore)).orElse(null);
		if(maxServer != null) {
			for (KFSeasonServer kfSeasonServer : serverList) {
				if(kfSeasonServer.getId() != maxServer.getId()) {
					System.err.println("和服赛季删除服务器:"+GSONUtils.ToJSONString(kfSeasonServer));
					KfSeasonServerUtils.delKFSeasonServer(kfSeasonServer.getId());
				}
			}
			if(mainServerId != maxServer.getId()) {
				int maxId = maxServer.getId();
				maxServer.setId(mainServerId);
				maxServer.loadName();
				maxServer.saveDB();
				KfSeasonServerUtils.delKFSeasonServer(maxId);
				KfSeasonServerUtils.reloadKFSeasonServerFromDB();
				System.err.println("和服赛季删除服务器:"+maxId+"->"+mainServerId);
			}
		}
	}
	
	
	public void firstLoadSeason() {
		for (ActivityItemContainer activityItemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
			try {
				KfExpeditionActivity activity = (KfExpeditionActivity) activityItemContainer.getAbstractActivity(ActivityType.KfExpeditionActivity);
				if(activity.isOpen()) {
					seasonBiz.doKFActOpen(activity.getServerId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
//		KfSeasonServerUtils.delKFSeasonServer(6);
//		doMergeSeason(6, Lists.newArrayList(7));
//		KfSeasonServerUtils.delKFSeasonServer(6);
	}
	
}
