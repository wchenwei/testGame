package com.hmkf.action.level;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.hm.config.GameConstants;
import com.hm.enums.KfLevelType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.config.KfRankConfig;
import com.hmkf.config.extra.KfPkLevelTemplate;
import com.hmkf.container.KFNpcContainer;
import com.hmkf.container.KFPlayerContainer;
import com.hmkf.db.KfDBUtils;
import com.hmkf.kfcenter.CenterBiz;
import com.hmkf.kfcenter.KfCenterData;
import com.hmkf.level.GameTypeGroup;
import com.hmkf.level.LevelGroupContainer;
import com.hmkf.level.LevelWorldGroup;
import com.hmkf.level.rank.RankGroup;
import com.hmkf.model.KFPlayer;
import com.hmkf.model.kfwarrecord.KfWarRecord;
import com.hmkf.redis.KFRankRedisUtils;
import com.hmkf.redis.RankItem;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 结算-重新分组
 * @date 2024/4/26 17:22
 */
@Slf4j
@Biz
public class KFLevelEndBiz {
    @Resource
    private LevelGroupContainer levelGroupContainer;
    @Resource
    private KfRankConfig kfRankConfig;
    @Resource
    private KfTopBiz kfTopBiz;

    public boolean doSnumEnd() {
        //清空数据
        doWeekClear();

        //结算本期数据
        for (LevelWorldGroup levelWorldGroup : levelGroupContainer.getAllLevelWorldGroup()) {
            try {
                doPKEnd(levelWorldGroup);
            } catch (Exception e) {
                log.error(levelWorldGroup.getGameTypeId() + "snum出现异常:", e);
            }
        }

        return true;
    }


    public void doPKEnd(LevelWorldGroup levelWorldGroup) {
        //key:段位id  val:此段位的所有人
        ArrayListMultimap<Integer, RankItem> levelMap = ArrayListMultimap.create();

        log.error(levelWorldGroup.getGameTypeId()+" start cal");
        Map<Integer, List<RankGroup>> groupMap = levelWorldGroup.getGameTypeGroup().getGroupMap();
        for (KfLevelType levelType : KfLevelType.AllLevelTypes) {
            List<RankGroup> rankGroupList = groupMap.get(levelType.getType());
            if(CollUtil.isEmpty(rankGroupList)) {
                continue;
            }
            log.error(levelWorldGroup.getGameTypeId()+" start levelType cal:"+levelType.getType());
            KfPkLevelTemplate levelTemplate = kfRankConfig.getStageGroup(levelType.getType());
            for (RankGroup rankGroup : rankGroupList) {
                log.error(levelWorldGroup.getGameTypeId()+" start rankGroup cal:"+rankGroup.getId());
                //取出当前排行数据
                List<RankItem> rankItemList = KFRankRedisUtils.getAllRankForRankItem(rankGroup.getRankId());
                for (RankItem rankItem : rankItemList) {
                    if(!rankItem.isNpc()) {//npc不加入
                        int newLv = levelTemplate.getNewLvType(rankItem.getRank());
                        System.out.println(GSONUtils.ToJSONString(rankItem)+"->"+newLv);
                        levelMap.put(newLv,rankItem);
                    }
                }
            }
        }
        log.error(levelWorldGroup.getGameTypeId()+" start doWeekClear");
        //清空
        levelWorldGroup.doWeekClear();

        log.error(levelWorldGroup.getGameTypeId()+" start group");
        //重新分组
        Table<String,String,Long> tablePlayerMap = HashBasedTable.create();
        GameTypeGroup gameTypeGroup = levelWorldGroup.getGameTypeGroup();
        for (KfLevelType levelType : KfLevelType.AllLevelTypes) {
            log.error(levelWorldGroup.getGameTypeId()+" start group levelType:"+levelType.getType());

            List<RankItem> rankItemList = levelMap.get(levelType.getType());
            if(CollUtil.isEmpty(rankItemList)) {
                continue;
            }
            for (RankItem rankItem : rankItemList) {
                KFPlayer player = KfDBUtils.getPlayerSports(rankItem.getIntId());
                if(isLosePlayer(player,levelType)) {
                    log.error(player.getId()+"->流失");
                    player.getLevelPlayerInfo().setMatchInfos(null);
                    player.getLevelPlayer().setRankId(null);
                    player.getLevelPlayer().SetChanged();
                    player.save();
                    continue;
                }
                RankGroup rankGroup = gameTypeGroup.getRankGroup(levelType.getType());
                //判断玩家是否流失
                player.getLevelPlayerInfo().setMatchInfos(null);
                player.getLevelPlayerInfo().setScore(KFLevelConstants.InitScore);
                player.getLevelPlayer().setLevelType(rankGroup.getRankId(),levelType.getType());
                player.save();

                rankGroup.addPlayerCount();//增加此组的人数
                //加入排行
                tablePlayerMap.put(rankGroup.getRankId(),rankItem.getPlayerId(),KFLevelConstants.InitScore);
            }
        }
        for (String rankId : tablePlayerMap.rowKeySet()) {
            Map<String,Long> playerMap = tablePlayerMap.row(rankId);
            KFRankRedisUtils.updateRank(rankId,playerMap);
        }
    }



    public void doWeekClear() {
        kfTopBiz.weekClear();
        KFNpcContainer.weekClear();
        KfWarRecord.clearDB();
    }

    public boolean isLosePlayer(KFPlayer player,KfLevelType levelType) {
        if(levelType != KfLevelType.Bronze) {
            return false;
        }

        PlayerRedisData redisData = RedisUtil.getPlayerRedisData(player.getId());
        if(System.currentTimeMillis() - redisData.getLastLoginDate() > 30* GameConstants.DAY) {
            log.error(player.getPlayerId()+"is lose");

            return true;
        }
        return false;
    }

}
