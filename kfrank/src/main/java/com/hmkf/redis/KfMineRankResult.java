package com.hmkf.redis;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.util.RandomUtils;
import com.hmkf.model.IPKPlayer;
import com.hmkf.model.KFPlayer;
import lombok.Data;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

@Data
public class KfMineRankResult {
    private long startRank;
    private long endRank;
    private long myRank;
    private List<RankItem> rankList = Lists.newArrayList();

    public KfMineRankResult(long myRank) {
        this.myRank = myRank;
        this.startRank = myRank - 10;
        this.endRank = myRank + 10;
    }

    public void loadRankList(Set<Tuple> tupList,int startRank) {
        for (Tuple tuple : tupList) {
            RankItem rankItem = new RankItem(tuple);
            rankItem.setRank(startRank++);
            rankList.add(rankItem);
        }
    }

    public void removeSelf(IPKPlayer player) {
        final String id = player.getPlayerId();
        this.rankList.removeIf(e -> StrUtil.equals(id,e.getPlayerId()));
    }

    public RankItem[] findMatchids(IPKPlayer player) {
        removeSelf(player);//删除自己
        RankItem[] luckMatchIds = new RankItem[5];
        List<RankItem> rankItemList = RandomUtils.randomEleList(rankList,luckMatchIds.length);
        for (int i = 0; i < rankItemList.size(); i++) {
            luckMatchIds[i] = rankItemList.get(i);
        }
        return luckMatchIds;
    }
}
