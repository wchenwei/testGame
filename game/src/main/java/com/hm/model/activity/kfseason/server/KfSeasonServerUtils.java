package com.hm.model.activity.kfseason.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.config.GameConstants;
import com.hm.enums.KfType;
import com.hm.redis.RedisKeyUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 服务器赛季积分排行
 * @author siyunlong  
 * @date 2020年12月9日 下午4:12:53 
 * @version V1.0
 */
public class KfSeasonServerUtils {
	private static Map<Integer,List<KFSeasonServer>> sortServerListMap = Maps.newHashMap();
	private static long lastUpdateTime;//10分钟刷新一次
	/**
	 * 获取组内排行
	 * @param groupId
	 * @return
	 */
	public static List<KFSeasonServer> getSortServerList(int groupId) {
		if(System.currentTimeMillis() - lastUpdateTime > 10*GameConstants.MINUTE) {
			reloadKFSeasonServerFromDB();
		}
		List<KFSeasonServer> sortServerList = sortServerListMap.get(groupId);
		if(sortServerList == null) {
			sortServerList = Lists.newArrayList();
			sortServerListMap.put(groupId, sortServerList);
		}
		return sortServerList;
	}
	
	/**
	 * 获取服务器在组内的排名
	 * @param serverId 服务器id
	 * @param seasonId 赛季id
	 * @param groupId 组id
	 * @return
	 */
	public static List<KFSeasonServer> getAllServerRank(int serverId,int seasonId,int groupId) {
		String tableName = "KFSeasonServer"+seasonId;
		return KFSeasonServer.getHashList(tableName, KFSeasonServer.class)
				.stream().filter(e -> e.getGroupId() == groupId)
				.sorted(Comparator.comparingInt(KFSeasonServer::getScore).reversed().thenComparing(KFSeasonServer::getLastTime))
				.collect(Collectors.toList());
	}
	
	/**
	 * 加载排行
	 */
	public static void reloadKFSeasonServerFromDB() {
		KFSeason season = KFSeasonUtil.getCurSeason();
		if(season == null) {
			return;
		}
		String tableName = "KFSeasonServer"+KFSeasonUtil.getCurSeason().getId();
		List<KFSeasonServer> allList = KFSeasonServer.getHashList(tableName, KFSeasonServer.class);
		sortServerListMap = allList
				.stream().sorted(Comparator.comparingInt(KFSeasonServer::getScore).reversed().thenComparing(KFSeasonServer::getLastTime))
		.collect(Collectors.groupingBy(KFSeasonServer::getGroupId));
		for (int groupId : sortServerListMap.keySet()) {
			for (KFSeasonServer kfSeasonServer : sortServerListMap.get(groupId)) {
				System.err.println(groupId+"赛季排名:"+kfSeasonServer.getId()+"="+kfSeasonServer.getScore());
			}
		}
		lastUpdateTime = System.currentTimeMillis();
	}
	
	/**
	 * 添加服务器赛季积分信息
	 * @param seasonServer
	 */
	public static void addKFSeasonServer(KFSeasonServer seasonServer) {
		getSortServerList(seasonServer.getGroupId()).add(seasonServer);
	}
	
	
	public static KFSeasonServer getKFSeasonServer(int seasonId,int serverId) {
		return KFSeasonServer.getHashVal(KFSeasonServer.buildHashKey(seasonId), serverId+"", KFSeasonServer.class);
	}
	public static KFSeasonServer getCurSeasonKFSeasonServer(int serverId) {
		return getKFSeasonServer(KFSeasonUtil.getCurSeason().getId(), serverId);
	}
	public static void delKFSeasonServer(int serverId) {
		String hashKey = RedisKeyUtils.buildKeyName(KFSeasonServer.buildHashKey(KFSeasonUtil.getCurSeason().getId()));
		KFSeasonServer.delHashField(hashKey, serverId+"");
	}
	public static void main(String[] args) {
//		reloadKFSeasonServerFromDB();
		List<KFSeasonServer> serverList = Lists.newArrayList();
		serverList.add(new KFSeasonServer(1, 1, 1));
		serverList.add(new KFSeasonServer(2, 1, 1));
		for (KFSeasonServer kfSeasonServer : serverList) {
			kfSeasonServer.addServerScore(KfType.Manor, 1, true);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		KFSeasonServer seasonServer = new KFSeasonServer(3, 1, 1);
		seasonServer.addServerScore(KfType.Manor, 1, true);
		seasonServer.addServerScore(KfType.Manor, 1, true);
		serverList.add(seasonServer);
		
		List<KFSeasonServer> list = serverList.stream()
				.sorted(Comparator.comparingInt(KFSeasonServer::getScore).reversed().thenComparing(KFSeasonServer::getLastTime))
				.collect(Collectors.toList());
		for (KFSeasonServer kfSeasonServer : list) {
			System.err.println(GSONUtils.ToJSONString(kfSeasonServer));
		}
	}
}
