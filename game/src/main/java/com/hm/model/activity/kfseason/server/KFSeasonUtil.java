package com.hm.model.activity.kfseason.server;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 服务器赛季信息
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author siyunlong  
 * @date 2020年12月10日 上午11:26:31 
 * @version V1.0
 */
public class KFSeasonUtil {
	private static KFSeason curSeason = KFSeason.getKFSeasonTime();//当前赛季id
	private static Map<Integer,KFSeason> topSeasonMap = Maps.newConcurrentMap();
	/**
	 * 从新加载本赛季信息
	 */
	public static void reloadSeasonId() {
//		curSeason = KFSeason.getKFSeasonTime();//当前赛季id
	}
	public static KFSeason getCurSeason() {
		if(curSeason == null) {
			reloadSeasonId();
		}
		return curSeason;
	}
	
	public static KFSeason getSeasonFromCache(int id) {
		KFSeason result = topSeasonMap.get(id);
		if(result == null) {
			result = KFSeason.getKFSeasonTime(id);
			topSeasonMap.put(id, result);
		}
		return result;
	}
	
	public static void removeSeasonCache(int id) {
		topSeasonMap.remove(id);
	}
}
