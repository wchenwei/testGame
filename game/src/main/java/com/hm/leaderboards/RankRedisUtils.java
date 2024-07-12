package com.hm.leaderboards;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.config.GameConstants;
import com.hm.enums.RankType;
import com.hm.model.player.BasePlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

import java.util.*;
import java.util.stream.Collectors;

public class RankRedisUtils {
	/**
	 * 获取排名的列表
	 * @param serverId
	 * @param rankName
	 * @param start 开始排名
     * @param end 结束排名
	 * @return
	 */
	public static List<Tuple> getRankList(int serverId,String rankName,int start,int end) {
		Jedis jedis = null;
		try {
			int startIndex = Math.max(start-1, 0);
			jedis = RankRedisConfig.getInstance().getRankPool().getResource();
			rankName = createRankName(serverId, rankName);
			if(!jedis.exists(rankName)) {
				return Lists.newArrayList();
			}
			Set<Tuple> tupList = jedis.zrevrangeWithScores(rankName, startIndex, end-1);
			return Lists.newArrayList(tupList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RankRedisConfig.getInstance().returnResource(jedis);
		}
		return Lists.newArrayList();
	}
	public static void renameRankList(int serverId,List<String> rankNames) {
		for (String name : rankNames) {
			renameRank(serverId, name);
		}
	}

	public static boolean renameRank(int serverId,String rankName) {
		Jedis jedis = null;
		try {
			rankName = createRankName(serverId, rankName);
			jedis = RankRedisConfig.getInstance().getRankPool().getResource();
			if(!jedis.exists(rankName)) {
				System.err.println(rankName+"不存在");
				return false;
			}
			String newName = rankName +":"+createMark();
			return jedis.renamenx(rankName, newName) == 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RankRedisConfig.getInstance().returnResource(jedis);
		}
		return false;
	}

	public static long getPlayerRank(RankType rankType, BasePlayer player) {
		Jedis jedis = null;
		try {
			jedis = RankRedisConfig.getInstance().getRankPool().getResource();
			String rankName = createRankName(player.getServerId(), rankType.getRankName(player));

			Long r = jedis.zrevrank(rankName, player.getId() + "");
			return r == null ? -1 : r + 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RankRedisConfig.getInstance().returnResource(jedis);
		}
		return -1;
	}

	public static long getPlayerRank(String rankName, String id) {
		Jedis jedis = null;
		try {
			jedis = RankRedisConfig.getInstance().getRankPool().getResource();
			Long r = jedis.zrevrank(rankName, id);
			return r == null ? -1 : r + 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RankRedisConfig.getInstance().returnResource(jedis);
		}
		return -1;
	}


	public static void mergeServerRank(int srcServerId,List<Integer> subServerIds,String rankName) {
		List<String> subRanks = subServerIds.stream().map(e -> createRankName(e, rankName)).collect(Collectors.toList());
		mergeServerRank(createRankName(srcServerId, rankName), subRanks);
	}
	/**
	 * 合并排行榜
	 * @param srcRankName 合并的主排行
	 * @param subRanks 下属排行
	 */
	public static void mergeServerRank(String srcRankName,List<String> subRanks) {
		Jedis jedis = null;
		try {
			jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            HashSet<String> allRank = Sets.newHashSet(subRanks);
            allRank.add(srcRankName);
            ZParams params = new ZParams().aggregate(ZParams.Aggregate.MAX);
            jedis.zunionstore(srcRankName, params, allRank.toArray(new String[0]));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RankRedisConfig.getInstance().returnResource(jedis);
		}
	}

	/**
	 * 转移玩家排行排行数据
	 * @param playerId
	 * @param rankName 老排行
	 * @param newRankName 新排行
	 */
	public static void transferRank(long playerId,int serverId,String rankName,String newRankName) {
		Jedis jedis = null;
		try {
			jedis = RankRedisConfig.getInstance().getRankPool().getResource();
			String member = playerId+"";
			String oldRankName = createRankName(serverId,rankName);
			Double score = jedis.zscore(oldRankName,member);
			if(score == null) {
				return;
			}
			jedis.zrem(oldRankName,member);
			jedis.zadd(createRankName(serverId,newRankName),score,member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RankRedisConfig.getInstance().returnResource(jedis);
		}
	}

    public static String createMark() {
		return DateUtil.format(new Date(System.currentTimeMillis()-GameConstants.DAY), DatePattern.PURE_DATE_FORMAT);
	}

	public static String createDateMark(Date date){
		return DateUtil.format(date, DatePattern.PURE_DATE_FORMAT);
	}

    public static String createRankName(int serverId,String rankName) {
		return HdLeaderboardsService.createTableName(serverId, rankName);
	}

    public static List<LeaderboardInfo> getRankListForLeader(int serverId,String rankName,int start,int end) {
		List<LeaderboardInfo> resultList = Lists.newArrayList();
		List<Tuple> tempList = getRankList(serverId, rankName, start, end);
		for (int i = 0; i < tempList.size(); i++) {
			Tuple ele = tempList.get(i);
			resultList.add(new LeaderboardInfo(ele.getElement(), start+i, ele.getScore()));
		}
		return resultList;
	}

    public static void main(String[] args) {
//		System.err.println(renameRank(6, RankType.Shooting.getRankName()));
// 		System.err.println(GSONUtils.ToJSONString(getRankList(246, RankType.Combat.getRankName(), 1, 10)));
// 		System.err.println(GSONUtils.ToJSONString(getRankListForLeader(246, RankType.Combat.getRankName(), 1, 1000)));
	}
}
