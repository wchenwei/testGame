package com.hm.chsdk.event2.activity;

import com.google.common.collect.Lists;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.RankType;
import com.hm.model.player.Player;
import redis.clients.jedis.Tuple;

import java.util.List;

public class CHActkRankEvent extends CommonParamEvent {
    public List<CHRank> ranking_list;
    public int huodong_id;
    public int rankType;

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Activity, 4008, "ranking");
    }

    public void loadRank(List<Tuple> topRanks, RankType rankType, int actType) {
        this.ranking_list = build(topRanks);
        this.rankType = rankType.getType();
        this.huodong_id = actType;
    }

    public static List<CHRank> build(List<Tuple> topRanks) {
        List<CHRank> ranking_list = Lists.newArrayList();
        for (int i = 0; i < topRanks.size(); i++) {
            Tuple leaderboardInfo = topRanks.get(i);
            int rank = i + 1;
            ranking_list.add(new CHRank(leaderboardInfo, rank));
        }
        return ranking_list;
    }
}
