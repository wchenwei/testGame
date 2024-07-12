package com.hmkf.action.npc;

import com.google.common.collect.Maps;
import com.hm.config.GameRandomNameConfig;
import com.hm.libcore.annotation.Biz;
import com.hmkf.config.KfRankConfig;
import com.hmkf.config.template.NpcArenaExTemplate;
import com.hmkf.container.KFNpcContainer;
import com.hmkf.level.rank.RankGroup;
import com.hmkf.redis.KFRankRedisUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Biz
public class KFNpcBiz {
    @Resource
    private KfRankConfig kfRankConfig;
    @Resource
    private GameRandomNameConfig gameRandomNameConfig;
    //初始化排行组里的npc
    public void initRankGroupNpc(RankGroup rankGroup) {
        addNpcForDay(rankGroup,1);
    }

    public void addNpcForDay(RankGroup rankGroup,int day) {
        String rankId = rankGroup.getRankId();
        int levelType = rankGroup.getLevelType();

        //不能重复
        List<String> npcNameList = KFNpcContainer.getAllNpcName();

        Map<String,Long> npcScoreMap = Maps.newHashMap();
        List<NpcArenaExTemplate> npcList = kfRankConfig.getNpcIdList(levelType,day);
        for (NpcArenaExTemplate template : npcList) {
            String npcName = gameRandomNameConfig.randomName(npcNameList);
            NpcPlayer npcPlayer = new NpcPlayer(rankId,levelType,template);
            npcPlayer.setName(npcName);
            npcPlayer.save();
            KFNpcContainer.addNpcPlayer(npcPlayer);

            npcScoreMap.put(npcPlayer.getId(),npcPlayer.getScore());
            npcNameList.add(npcName);
            log.error(rankId+" init npc:"+npcPlayer.getId()+" day:"+day);
        }
        //加入排行榜
        KFRankRedisUtils.updateRank(rankId,npcScoreMap);
    }
}
