package com.hm.action.battle.biz;

import com.hm.action.battle.vo.PWPlayerVo;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.FieldBossRewardTemplateImpl;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.model.player.Player;
import com.hm.war.sg.FieldBossResult;
import com.hm.war.sg.WarParam;
import com.hm.war.sg.handler.WarBossHandler;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.NpcTroop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FieldBossBiz {
    @Resource
    private NpcConfig npcConfig;
    @Resource
    private CommValueConfig commValueConfig;


    public FieldBossResult doFight(List<AbstractFightTroop> playerTroops, FieldBossRewardTemplateImpl bossRewardTemplate, Integer index){
        int npcId = bossRewardTemplate.getNpcId(index);
        NpcTroop bossNpc = buildFieldBoss(npcId);
        FieldBossResult fieldBossResult = new FieldBossResult();
        WarParam warParam = buildWarParam(bossRewardTemplate.getId(), index);
        fieldBossResult.doWarFight(playerTroops, bossNpc, warParam);
        fieldBossResult.setAtkInfo(new PWPlayerVo(playerTroops.size()));
        fieldBossResult.setDefInfo(new PWPlayerVo(1));

        return fieldBossResult;
    }

    public NpcTroop buildFieldBoss(int npcId){
        NpcTroopTemplate npcTroopTemplate = npcConfig.getNpcTroopTemplate(npcId);
        NpcTroop npcTroop = new NpcTroop(npcId + "");
        npcTroop.loadNpcInfo(npcTroopTemplate);
        return npcTroop;
    }

    public boolean updatePlayerHurt(Player player, Date now,long totalHurt) {
        String dateMark = RankRedisUtils.createDateMark(now);
        if (player.playerFieldBoss().updateMaxHurt(totalHurt, dateMark)){
            HdLeaderboardsService.getInstance().updatePlayerRankOverride(player.getId(), player.getServerId(), RankType.FieldBoss.getRankName(now), totalHurt);
            return true;
        }
        return false;
    }

    private WarParam buildWarParam(Integer boosId, Integer index) {
        int maxFrame = commValueConfig.getCommValue(CommonValueType.BeastFightMaxFrame);
        return new WarParam().setMaxFrame(maxFrame).setWarHandler(new WarBossHandler(boosId, index));

    }

}
