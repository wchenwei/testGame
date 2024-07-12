package com.hmkf.action.level;

import cn.hutool.core.util.StrUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.enums.KfLevelType;
import com.hm.libcore.util.date.DateUtil;
import com.hmkf.action.level.vo.KfPlayerDetailVo;
import com.hmkf.action.npc.NpcPlayer;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.config.KfRankConfig;
import com.hmkf.config.template.NpcArenaExTemplate;
import com.hmkf.container.KFNpcContainer;
import com.hmkf.db.KfDBUtils;
import com.hmkf.kfcenter.CenterBiz;
import com.hmkf.kfcenter.KfCenterData;
import com.hmkf.leaderboard.KfLeaderBiz;
import com.hmkf.level.GameTypeGroup;
import com.hmkf.level.LevelGroupContainer;
import com.hmkf.level.LevelWorldGroup;
import com.hmkf.level.rank.RankGroup;
import com.hmkf.model.IPKPlayer;
import com.hmkf.model.KFPlayer;
import com.hmkf.redis.RankItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class KfLevelBiz {
    @Resource
    private CenterBiz centerBiz;
    @Resource
    private KfRankConfig kfRankConfig;
    @Resource
    private LevelGroupContainer levelGroupContainer;

    public void doDayZero(KFPlayer player) {
        KfCenterData kfCenterData = centerBiz.getCenterData();
        //判断每日重置
        String playerDayMark = player.getLevelPlayerInfo().getDayMark();
        if (!StrUtil.equals(playerDayMark, kfCenterData.getServerDayMark())) {
            log.error(player.getId() + ":每日重置");
            player.getLevelPlayerInfo().setDayMark(kfCenterData.getServerDayMark());
            player.getLevelPlayerInfo().dayReset();
            if (!DateUtil.isSameWeek(playerDayMark, kfCenterData.getServerDayMark())) {
                player.getLevelPlayerInfo().weekReset();
            }
        }
    }

    //进入跨服处理
    public void doEnterKF(KFPlayer player) {
        if(StrUtil.isNotEmpty(player.getRankId())) {
            return;
        }
        //分配组
        int gameTypeId = player.getGameTypeId();
        LevelWorldGroup levelWorldGroup = levelGroupContainer.getLevelWorldGroup(player);
        if (levelWorldGroup == null) {
            log.error("报名出错:找不到世界组=" + gameTypeId);
            return;
        }
        GameTypeGroup gameTypeGroup = levelWorldGroup.getGameTypeGroup();
        synchronized (gameTypeGroup) {
            RankGroup rankGroup = gameTypeGroup.getRankGroup(KfLevelType.Bronze.getType());
            rankGroup.addPlayerCount();//增加此组的人数

            player.getLevelPlayerInfo().setScore(KFLevelConstants.InitScore);
            player.getLevelPlayer().setLevelType(rankGroup.getRankId(),KfLevelType.Bronze.getType());
        }
        //添加排行
        KfLeaderBiz.updatePlayerRank(player);
        checkRandomMatchUser(player);
    }

    public void checkRandomMatchUser(KFPlayer player) {
        if(player.getLevelPlayerInfo().getMatchInfos() != null) {
            return;
        }
        player.getLevelPlayerInfo().setMatchInfos(buildMatchUser(player));
        player.save();
    }


    public KfPlayerDetailVo[] buildMatchUser(KFPlayer player) {
        //随机的玩家数据
        RankItem[] matchInfos = KfLeaderBiz.findFightPlayerId(player);

        KfPlayerDetailVo[] resultList = new KfPlayerDetailVo[matchInfos.length];
        for (int i = 0; i < resultList.length; i++) {
            resultList[i] = buildKfPlayerDetailVo(player.getRankId(),matchInfos[i]);
            if(resultList[i] != null) {
                resultList[i].setRank(matchInfos[i].getRank());
            }
        }
        return resultList;
    }


    public KfPlayerDetailVo buildKfPlayerDetailVo(String rankId,RankItem rankItem) {
        if(rankItem == null) {
            return null;
        }
        if (rankItem.isNpc()) {
            NpcPlayer npc = KFNpcContainer.getNpcPlayer(rankItem.getPlayerId());
            if(npc == null) {
                npc = KFNpcContainer.createNpcForError(rankId,rankItem);
            }
            NpcArenaExTemplate arenaExTemplate = kfRankConfig.getArenaNpc(npc.getNpcId());
            NpcTroopTemplate template = arenaExTemplate.getFirstNpcTemplate();

            return new KfPlayerDetailVo(npc, template,arenaExTemplate.getPower());
        } else {
            KFPlayer player = KfDBUtils.getPlayerSports(rankItem.getIntId());
            if(player == null) {
                return null;
            }
            return new KfPlayerDetailVo(player);
        }
    }


    public void addPlayerScore(IPKPlayer player, long addScore) {
        if (player == null) {
            return;
        }
        player.addScore(addScore);
        KfLeaderBiz.updatePlayerRank(player);
    }

    public boolean isCanFightTime() {
        long startTime = DateUtil.beginOfWeek(new Date()).getTime() + KFLevelConstants.WeekSHour * GameConstants.HOUR;
        long now = System.currentTimeMillis();
        return now > startTime;
    }
}
