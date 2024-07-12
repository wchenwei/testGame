package com.hmkf.action.npc;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.util.RandomUtils;
import com.hm.war.sg.CLunWarResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hmkf.action.level.KfLevelBiz;
import com.hmkf.action.level.KfLevelFightBiz;
import com.hmkf.container.KFNpcContainer;
import com.hmkf.level.LevelGroupContainer;
import com.hmkf.level.LevelWorldGroup;
import com.hmkf.level.rank.RankGroup;
import com.hmkf.model.IPKPlayer;
import com.hmkf.model.kfwarrecord.AtkRecord;
import com.hmkf.redis.KFRankRedisUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: npc定时器检查
 * @date 2024/4/25 17:33
 */
@Slf4j
@Biz
public class NpcCheckBiz {
    @Resource
    private KfLevelFightBiz kfLevelFightBiz;
    @Resource
    private KfLevelBiz kfLevelBiz;
    @Resource
    private LevelGroupContainer levelGroupContainer;
    @Resource
    private KFNpcBiz kFNpcBiz;

    public static List<Integer> WMSList = IntStream.range(5,1430).boxed().collect(Collectors.toList());
    //周一的npc战斗时间
    public static List<Integer> WeekList = IntStream.range(8*60+5,1430).boxed().collect(Collectors.toList());


    //每天凌晨检查生成今日npc
    public void doCheckAdd() {
        if(DateUtil.getCsWeek() == 1) {
            return;
        }
        for (LevelWorldGroup levelWorldGroup : levelGroupContainer.getAllLevelWorldGroup()) {
            try {
                Map<Integer, List<RankGroup>> groupMap = levelWorldGroup.getGameTypeGroup().getGroupMap();
                for (int lvType : groupMap.keySet()) {
                    try {
                        for (RankGroup rankGroup : groupMap.get(lvType)) {
                            doCheckAddNpcForGroup(rankGroup);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doCheckAddNpcForGroup(RankGroup rankGroup) {
        int createDay = (int)rankGroup.getCreateDay();//此组创建多少天了
        if(KFNpcContainer.npcMap.values().stream().filter(e -> e.getLevelType() == rankGroup.getLevelType())
                .anyMatch(e -> e.getDay() == createDay)) {
            return;//已经有了
        }
        kFNpcBiz.addNpcForDay(rankGroup,createDay);
    }


    public void doCheck() {
        if (!kfLevelBiz.isCanFightTime()) {
            return;
        }
        int minute = DateUtil.thisHour(true)*60 + DateUtil.thisMinute();
        for (NpcPlayer value : KFNpcContainer.npcMap.values()) {
            try {
                doNpcCheck(value,minute);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doNpcCheck(NpcPlayer npcPlayer,int minute) {
        if(!CollUtil.contains(npcPlayer.getWlist(),minute)) {
            return;
        }
        //开始战斗
        String defId = findDefId(npcPlayer);
        if(defId == null) {
            log.error(npcPlayer.getPlayerId()+" npc找不到对手");
            return;
        }
        doNpcFight(npcPlayer,Integer.parseInt(defId));
    }

    public String findDefId(NpcPlayer npcPlayer) {
        List<String> playerIdList = KFRankRedisUtils.findMatchList(npcPlayer).getRankList().stream()
                .filter(e -> !e.isNpc())
                .map(e -> e.getPlayerId())
                .collect(Collectors.toList());
        if(CollUtil.isNotEmpty(playerIdList)) {
            return RandomUtils.randomEle(playerIdList);
        }
        return null;
    }

    public void doNpcFight(NpcPlayer npcPlayer,long playerId) {
        List<AbstractFightTroop> atkTroops = kfLevelFightBiz.buildNpcFightTroops(npcPlayer.getId());
        AtkRecord atkRecord = new AtkRecord(npcPlayer.getPlayerId(),null);
        List<AbstractFightTroop> defList = kfLevelFightBiz.buildDefList(playerId+"");
        //构建防御部队列表
        IPKPlayer defPlayer = kfLevelFightBiz.getPKPlayer(playerId+"");

        CLunWarResult result = kfLevelFightBiz.doFight(npcPlayer,defPlayer,atkTroops,defList,atkRecord,false);
        if(result.isAtkWin()) {
            defPlayer.save();
        }
        log.error("npc 战斗:"+npcPlayer.getId()+"->"+playerId+" ->"+result.isAtkWin());
    }



    public static List<Integer> randomWList() {
        int count = RandomUtils.randomInt(5,8);
        List<Integer> list = DateUtil.getCsWeek() == 1?WeekList:WMSList;
        return RandomUtils.randomEleList(list,count);
    }

}
