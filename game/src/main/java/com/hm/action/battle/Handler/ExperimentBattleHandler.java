package com.hm.action.battle.Handler;

import com.hm.action.troop.client.ClientTroop;
import com.hm.config.MissionConfig;
import com.hm.config.excel.templaextra.MissionSpecailExtraTemplate;
import com.hm.config.excel.templaextra.MissionTypeTemplateImpl;
import com.hm.libcore.annotation.Biz;
import com.hm.model.battle.ExperimentBattle;
import com.hm.model.fight.FightProxy;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.NpcTroop;
import com.hm.war.sg.troop.PlayerTroop;

import javax.annotation.Resource;
import java.util.List;

/**
 * 征战蛮荒
 */
@Biz
public class ExperimentBattleHandler extends AbstractBattleHandler<ExperimentBattle>{

    @Resource
    private MissionConfig missionConfig;

    @Override
    public boolean isCanFight(Player player, ExperimentBattle battle, int id) {
        if (!super.isCanFight(player, battle, id)){
            return false;
        }
        //根据配置获取所属战役
        MissionSpecailExtraTemplate battleTemplate = missionConfig.getBattle(id);
        if(player.playerCommander().getMilitaryLv()<battleTemplate.getUnlock_level()){
            return false;//等级不足
        }
        return true;
    }


    @Override
    public void doFightAfter(Player player, WarResult warResult, ExperimentBattle battle, int id) {
        if (warResult.isAtkWin()){
            battle.setCurId(id);
            player.playerBattle().SetChanged();
        }
    }

    @Override
    public List<Items> getFightReward(ExperimentBattle battle, WarResult warResult, int id) {
        if (warResult.isAtkWin()){
            MissionSpecailExtraTemplate template = missionConfig.getBattle(id);
            return template.getFirstRewards();
        }
        return null;
    }

    @Override
    public boolean checkCanDayReward(Player player, ExperimentBattle battle) {
        MissionTypeTemplateImpl typeTemplate = missionConfig.getMissionTypeTemplate(battle.getBattleId());
        return battle.getDayRewardCount() < typeTemplate.getDay_reward();
    }

    @Override
    public List<Items> getDayRewards(Player player, ExperimentBattle battle) {
        int battleId = battle.getBattleId();
        MissionSpecailExtraTemplate template = missionConfig.getDayRewardMissionSpecialTemplate(battleId, battle.getCurId());
        if (template != null){
            return template.getDayRewards();
        }
        return null;
    }

    @Override
    public void doDayReward(Player player, ExperimentBattle battle) {
        battle.addDayRewardCount();
        player.playerBattle().SetChanged();
    }
}
