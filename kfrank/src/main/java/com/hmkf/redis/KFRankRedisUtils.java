package com.hmkf.redis;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.leaderboards.RankRedisConfig;
import com.hm.libcore.util.date.DateUtil;
import com.hm.redis.type.RedisTypeEnum;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.leaderboard.KfLeaderBiz;
import com.hmkf.model.IPKPlayer;
import com.hmkf.model.KFPlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class KFRankRedisUtils {

    public static KfMineRankResult findMatchList(IPKPlayer player) {
        return findMatchList(player.getRankId(), player.getPlayerId());
    }

    public static List<RankItem> getAllRankForRankItem(String rankId) {
        List<RankItem> rankItemList = Lists.newArrayList();
        List<Tuple> tupleList = getAllRank(rankId);
        for (int i = 0; i < tupleList.size(); i++) {
            RankItem rankItem = new RankItem(tupleList.get(i));
            rankItem.setRank(i+1);
            rankItemList.add(rankItem);
        }
        return  rankItemList;
    }
    public static List<Tuple> getAllRank(String rankId) {
        String rankName = KfLeaderBiz.buildRankName(rankId);
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            return Lists.newArrayList(jedis.zrevrangeWithScores(rankName, 0, Integer.MAX_VALUE));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RankRedisConfig.getInstance().returnResource(jedis);
        }
        return Lists.newArrayList();
    }

    public static KfMineRankResult findMatchList(String rankId, Object playerId) {
        String rankName = KfLeaderBiz.buildRankName(rankId);
        String memberId = playerId.toString();
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            Long rank = jedis.zrevrank(rankName, memberId);
            if (rank == null) {
                jedis.zadd(rankName, 0, memberId);
                rank = jedis.zrevrank(rankName, memberId);
            }
            KfMineRankResult rankResult = new KfMineRankResult(rank + 1);
            long startRank = Math.max(rankResult.getStartRank() - 1, 0);
            long endRank = rankResult.getEndRank() - 1;

            Set<Tuple> tupList = jedis.zrevrangeWithScores(rankName, startRank, endRank);
            rankResult.loadRankList(tupList,(int)startRank+1);
            return rankResult;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RankRedisConfig.getInstance().returnResource(jedis);
        }
        return null;
    }
    public static List<RankItem> findTop(String rankId, int top){
        String rankName = KfLeaderBiz.buildRankName(rankId);
        List<RankItem> topList = Lists.newArrayList();
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            Set<Tuple> tupList = jedis.zrevrangeWithScores(rankName, 0, top-1);
            for (Tuple tuple : tupList) {
                topList.add(new RankItem(tuple));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RankRedisConfig.getInstance().returnResource(jedis);
        }
        return topList;
    }

    public static void updateRank(String rankId,String id,long score) {
        String rankName = KfLeaderBiz.buildRankName(rankId);
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            double v = DateUtil.parseTimeDouble(score);
            jedis.zadd(rankName,v,id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RankRedisConfig.getInstance().returnResource(jedis);
        }
    }
    public static void updateRank(String rankId,Map<String,Long> playerMap) {
        if(CollUtil.isEmpty(playerMap)) {
            return;
        }
        String rankName = KfLeaderBiz.buildRankName(rankId);
        Map<String, Double> scoreMap  = Maps.newHashMap();
        for (Map.Entry<String, Long> entry : playerMap.entrySet()) {
            scoreMap.put(entry.getKey(),DateUtil.parseTimeDouble(entry.getValue()));
        }
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            jedis.zadd(rankName,scoreMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RankRedisConfig.getInstance().returnResource(jedis);
        }
    }

    public static long getRankCount(String rankId) {
        String rankName = KfLeaderBiz.buildRankName(rankId);
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            return jedis.zcard(rankName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RankRedisConfig.getInstance().returnResource(jedis);
        }
        return 0;
    }
    public static long getPlayerRank(String rankId,String id) {
        String rankName = KfLeaderBiz.buildRankName(rankId);
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            return jedis.zrevrank(rankName,id)+1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RankRedisConfig.getInstance().returnResource(jedis);
        }
        return 99999;
    }
    public static Map<String,Long> getPlayerRank(String rankId,List<String> ids) {
        Map<String,Long> rankMap = Maps.newHashMap();
        String rankName = KfLeaderBiz.buildRankName(rankId);
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            for (String id : ids) {
                long rank = jedis.zrevrank(rankName,id);
                if(rank >= 0) {
                    rankMap.put(id,rank+1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RankRedisConfig.getInstance().returnResource(jedis);
        }
        return rankMap;
    }
}
