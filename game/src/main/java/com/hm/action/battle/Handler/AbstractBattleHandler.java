package com.hm.action.battle.Handler;

import com.hm.action.troop.client.ClientTroop;
import com.hm.config.MissionConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.MissionSpecailExtraTemplate;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.battle.BaseBattle;
import com.hm.model.fight.FightProxy;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.NpcTroop;
import com.hm.war.sg.troop.PlayerTroop;

import java.util.List;

public abstract class AbstractBattleHandler<T extends BaseBattle> {

    //判断是否可对此战役战斗
    public boolean isCanFight(Player player, T battle, int id){
        if(battle == null){
            return false;//战役不存在
        }
        if (!battle.isCanFight(player, id)){
            return false;
        }
        return true;
    }

    // 战斗
    public WarResult fight(Player player, T battle, ClientTroop troop, int id) {
        PlayerTroop atkTroop = new PlayerTroop(player.getId(), "battle");
        atkTroop.loadClientTroop(troop);
        MissionConfig missionConfig = SpringUtil.getBean(MissionConfig.class);
        MissionSpecailExtraTemplate template = missionConfig.getBattle(id);
        NpcTroop defNpcTroop = buildNpcTroop(battle, template.getNpcId());
        return new FightProxy().doFight(atkTroop, defNpcTroop);
    }

    // 战斗后处理数据
    public abstract void doFightAfter(Player player, WarResult warResult, T battle, int id);

    // 获取战斗奖励
    public abstract List<Items> getFightReward(T battle, WarResult warResult, int id);

    // 是否可以领取每日奖励
    public boolean checkCanDayReward(Player player, T battle){
        return false;
    }

    // 领取每日奖励
    public List<Items> getDayRewards(Player player, T battle){
        return null;
    }

    // 领取奖励后处理
    public void doDayReward(Player player, T battle){
    }

    // 是否可以扫荡
    public boolean isCanSweep(Player player, T battle, int id){
        return false;
    }

    // 扫荡
    public List<Items> sweep(Player player, T battle, int id){
        return null;
    }

    public NpcTroop buildNpcTroop(T battle, int npcId){
        NpcConfig npcConfig = SpringUtil.getBean(NpcConfig.class);
        NpcTroop npcTroop = new NpcTroop(npcId+"");
        NpcTroopTemplate npcTroopTemplate = npcConfig.getNpcTroopTemplate(npcId);
        npcTroop.loadNpcInfo(npcTroopTemplate);
        return npcTroop;
    }

}
