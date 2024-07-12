package com.hmkf.leaderboard;

import com.google.common.collect.ArrayListMultimap;
import com.hm.config.excel.RankConfig;
import com.hm.config.excel.RankReward;
import com.hm.config.excel.templaextra.RankTemplate;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.libcore.annotation.Biz;
import com.hmkf.model.IPKPlayer;
import com.hmkf.redis.KFRankRedisUtils;
import com.hmkf.redis.KfMineRankResult;
import com.hmkf.redis.RankItem;
import com.hmkf.server.ServerGroupManager;
import com.hmkf.util.ServerHttpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Biz
public class KfLeaderBiz {
    @Resource
    private RankConfig rankConfig;


    public static String buildRankName(String groupId) {
        return HdLeaderboardsService.createLeaderMark(0) + ":" + groupId;
    }

    public static void updatePlayerRank(IPKPlayer player) {
        long score = player.getScore();
        KFRankRedisUtils.updateRank(player.getRankId(),player.getPlayerId(),score);
    }


    public static RankItem[] findFightPlayerId(IPKPlayer player) {
        KfMineRankResult mineRankResult = KFRankRedisUtils.findMatchList(player);
        if (mineRankResult == null) {
            return new RankItem[5];
        }
        return mineRankResult.findMatchids(player);
    }

    public void doRankReward(int leaderIndex, RankType rankType) {
        RankReward rankReward = rankConfig.getRankReward(rankType);
        if (rankReward == null) {
            log.error("处理排行奖励出错每日奖励:" + rankType.getDesc());
            return;
        }
        ArrayListMultimap<Integer, KfPlayerRank> serverMap = ArrayListMultimap.create();
        List<LeaderboardInfo> topRanks = HdLeaderboardsService.getInstance().getGameRank(leaderIndex, rankType.getRankName(), 1, rankReward.getMaxRank());
        for (LeaderboardInfo leaderboardInfo : topRanks) {
            RankTemplate rankTemplate = rankReward.getRankTemplate(leaderboardInfo.getRank());
            if (rankTemplate != null) {
                if (leaderboardInfo.getIntId() <= 0) {
                    continue;
                }
                int serverId = ServerGroupManager.getIntance().getServerDbId(leaderboardInfo.getIntId());
                serverMap.put(serverId, new KfPlayerRank(leaderboardInfo.getIntId(), leaderboardInfo.getRank()));
            }
        }
        for (int serverId : serverMap.keySet()) {
            List<KfPlayerRank> rankList = serverMap.get(serverId);
            ServerHttpUtils.sendRankReward(rankType.getType(), serverId, rankList);
        }
    }

}
