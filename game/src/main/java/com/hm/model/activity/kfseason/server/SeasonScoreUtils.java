package com.hm.model.activity.kfseason.server;

import com.google.common.collect.HashMultiset;
import com.hm.enums.KfType;
import com.hm.util.ServerUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description: 赛季积分-跨服添加
 * @author siyunlong  
 * @date 2020年12月10日 下午1:56:27 
 * @version V1.0
 */
public class SeasonScoreUtils {
	public static final int YzWheelset = 4;//远征轮空积分
	public static final int ScoreWheelset = 4;//积分战轮空积分
	public static final int WzWheelset = 4;//
	public static final int PolarWheelset = 4;//
	public static final int ManorWheelset = 4;//
	
	//跨服军演排名积分
	public static final int[] KFSportScores = {6,5,4,4,3,3,3,3,
			 2,2,2,2,2,2,2,2,
			 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
			 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
			 };
	public static final int[] KFScoreScores = {10,6,2};
	public static final int[] KFYzScores = {2,3,5,7,9,12};
	public static final int[] KFWzScores = {10,2};
	public static final int[] KFPolarScores = {10,2};
	public static final int[] KFManorScores = {15,12,10,8,7,6,5,4,3,2};
	/**
	 * 添加跨服军演积分
	 * @param rankMap
	 */
	public static void addSportScore(Map<Integer,Integer> rankMap) {
		HashMultiset<Integer> scoreMap = HashMultiset.create();
		for (Map.Entry<Integer,Integer> entry : rankMap.entrySet()) {
			long playerId = entry.getKey();
			int score = getSportScores(entry.getValue());
			if(score > 0) {
				scoreMap.add(ServerUtils.getServerId(playerId), score);
			}
		}
		for (int serverId : scoreMap.elementSet()) {
			KFSeasonServer seasonServer = KfSeasonServerUtils.getCurSeasonKFSeasonServer(serverId);
			if(seasonServer == null) {
				System.err.println(serverId+"不存在====");
				continue;
			}
			System.err.println(seasonServer.getId()+"赛季积分跨服军演:"+scoreMap.count(serverId));
			seasonServer.addServerScore(KfType.Sports, scoreMap.count(serverId), false);
			seasonServer.saveDB();
		}
	}
	public static int getSportScores(int rank) {
		if(rank <= 0 || rank > KFSportScores.length) {
			return 0;
		}
		return KFSportScores[rank-1];
	}
	
	/**
	 * 添加跨服积分
	 * @param rankServerList
	 */
	public static void addKFScoreScore(List<Integer> rankServerList) {
		for (int i = 0; i < Math.min(KFScoreScores.length, rankServerList.size()); i++) {
			if(KFScoreScores[i] > 0) {
				KFSeasonServer seasonServer = KfSeasonServerUtils.getCurSeasonKFSeasonServer(rankServerList.get(i));
				if(seasonServer == null) {
					System.err.println(rankServerList.get(i)+"不存在====");
					continue;
				}
				System.err.println(seasonServer.getId()+"赛季积分积分:"+i);
				seasonServer.addServerScore(KfType.Score, KFScoreScores[i], i==0);
				seasonServer.saveDB();
			}
		}
	}
	
	/**
	 * 跨服远征积分
	 * @param serverId
	 * @param citySize
	 * @param occMain
	 */
	public static void addKFYzScore(int serverId,int citySize,boolean occMain) {
		int boxSize = Math.max(citySize, 0);
		System.err.println(serverId+"远征赛季积分:"+boxSize);
		int score = KFYzScores[Math.min(boxSize, KFYzScores.length-1)];
		KFSeasonServer seasonServer = KfSeasonServerUtils.getCurSeasonKFSeasonServer(serverId);
		if(seasonServer == null) {
			System.err.println(serverId+"不存在====");
			return;
		}
		seasonServer.addServerScore(KfType.KfExpedetion, score, occMain);
		seasonServer.saveDB();
	}
	
	public static void addWzScore(int serverId,boolean occMain) {
		if(serverId <= 0) {
			return;
		}
		KFSeasonServer seasonServer = KfSeasonServerUtils.getCurSeasonKFSeasonServer(serverId);
		if(seasonServer == null) {
			System.err.println(serverId+"不存在====");
			return;
		}
		System.err.println(serverId+"赛季积分王者:"+occMain);
		int score = KFWzScores[occMain?0:1];
		seasonServer.addServerScore(KfType.KfKingCanyon, score, occMain);
		seasonServer.saveDB();
	}
	public static void addJzldScore(int serverId,boolean occMain) {
		if(serverId <= 0) {
			return;
		}
		System.err.println(serverId+"赛季积分极地乱斗:"+occMain);
		KFSeasonServer seasonServer = KfSeasonServerUtils.getCurSeasonKFSeasonServer(serverId);
		if(seasonServer == null) {
			System.err.println(serverId+"不存在====");
			return;
		}
		int score = KFPolarScores[occMain?0:1];
		seasonServer.addServerScore(KfType.KfScuffle, score, occMain);
		seasonServer.saveDB();
	}
	public static void addKfManorScore(int serverId,int rank) {
		if(serverId <= 0) {
			return;
		}
		KFSeasonServer seasonServer = KfSeasonServerUtils.getCurSeasonKFSeasonServer(serverId);
		if(seasonServer == null) {
			System.err.println(serverId+"不存在====");
			return;
		}
		System.err.println(serverId+"赛季积分领地排名:"+rank);
        int score = 2;
		if(rank > 0 && rank < KFManorScores.length) {
			score = KFManorScores[rank-1];
		}
		seasonServer.addServerScore(KfType.Manor, score, rank==1);
		seasonServer.saveDB();
	}
	
	public static void addServerScore(int serverId,KfType kfType,int score) {
		System.err.println(serverId+"轮空:"+kfType.getDesc()+" :"+score);
		KFSeasonServer seasonServer = KfSeasonServerUtils.getCurSeasonKFSeasonServer(serverId);
		if(seasonServer == null) {
			System.err.println(serverId+"不存在====");
			return;
		}
		seasonServer.addServerScore(kfType, score, false);
		seasonServer.saveDB();
	}
	
	public static void main(String[] args) {
		int serverId = 118;
		KFSeasonServer seasonServer = new KFSeasonServer(serverId,1,1);
		seasonServer.saveDB();
		SeasonScoreUtils.addJzldScore(serverId, true);
//		SeasonScoreUtils.addKfManorScore(serverId, 1);
//		SeasonScoreUtils.addKfManorScore(serverId, 2);
//		SeasonScoreUtils.addKFScoreScore(Lists.newArrayList(serverId));
//		SeasonScoreUtils.addKFYzScore(serverId, 2, true);
//		SeasonScoreUtils.addWzScore(serverId, true);
		
//		addServerScore(serverId, KfType.KfExpedetion, 4);
	}
}
