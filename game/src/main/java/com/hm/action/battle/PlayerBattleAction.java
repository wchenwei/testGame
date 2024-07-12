package com.hm.action.battle;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.battle.Handler.AbstractBattleHandler;
import com.hm.action.item.ItemBiz;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.MissionConfig;
import com.hm.config.excel.templaextra.MissionTypeTemplateImpl;
import com.hm.enums.BattleType;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.battle.BaseBattle;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.WarResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Action
public class PlayerBattleAction extends AbstractPlayerAction {

    @Resource
    private MissionConfig missionConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private LogBiz logBiz;

    /**
     * 战斗
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_Battle_Fight)
    public void fight(Player player, JsonMsg msg){
        int id = msg.getInt("id");
        int type = msg.getInt("type");
        ClientTroop clientTroop = ClientTroop.buildFull(player, msg);
        if (CollUtil.isEmpty(clientTroop.getArmyList())){
            player.sendErrorMsg(SysConstant.Battle_Troop_Not);
            return;
        }
        BattleType battleType = BattleType.getBattleType(type);
        MissionTypeTemplateImpl typeTemplate = missionConfig.getMissionTypeTemplate(battleType.getType());
        // 是否对玩家开放
        if(!typeTemplate.isOpen(player)) {
            player.sendErrorMsg(SysConstant.Mission_Fight_Not_Open);
            return;//未开放
        }
        BaseBattle battle = player.playerBattle().getPlayerBattle(type);
        AbstractBattleHandler battleHandler = battleType.getBattleHandler();
        // 检查是否可以战斗
        if (!battleHandler.isCanFight(player, battle, id)){
            player.sendErrorMsg(SysConstant.Mission_Fight_Not);
            return;
        }
        // 战斗
        WarResult result = battleHandler.fight(player, battle, clientTroop, id);
        // 战斗后处理
        battleHandler.doFightAfter(player, result, battle, id);
        player.notifyObservers(ObservableEnum.BattleFight, type, id, result.isAtkWin());
        List<Items> fightRewards = battleHandler.getFightReward(battle, result, id);
        if (CollUtil.isNotEmpty(fightRewards)){
            itemBiz.addItem(player, fightRewards, LogType.Battle.value(type+"_"+id));
        }
        logBiz.addPlayerBattleLog(player, type, id, player.getCombat(),result.isAtkWin() ? 1: 0);
        JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Player_Battle_Fight);
        serverMsg.addProperty("id", id);
        serverMsg.addProperty("type", type);
        serverMsg.addProperty("result", result);
        serverMsg.addProperty("rewards", fightRewards);
        player.sendUserUpdateMsg();
        player.sendMsg(serverMsg);

    }


    /**
     * 每日奖励领取
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_Battle_DayReward)
    public void dayReward(Player player, JsonMsg msg){
        int type = msg.getInt("type");
        BattleType battleType = BattleType.getBattleType(type);

        BaseBattle battle = player.playerBattle().getPlayerBattle(type);
        AbstractBattleHandler battleHandler = battleType.getBattleHandler();
        // 检查是否可以领取奖励
        if (!battleHandler.checkCanDayReward(player, battle)){
            player.sendErrorMsg(SysConstant.Day_Reward_Not);
            return;
        }
        List<Items> rewards = battleHandler.getDayRewards(player, battle);
        // 没有奖励
        if (CollUtil.isEmpty(rewards)){
            player.sendErrorMsg(SysConstant.Day_Reward_Not);
            return;
        }
        battleHandler.doDayReward(player, battle);
        itemBiz.addItem(player, rewards, LogType.BattleDayReward.value(type));
        JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Player_Battle_DayReward);
        serverMsg.addProperty("rewards", rewards);
        player.sendUserUpdateMsg();
        player.sendMsg(serverMsg);

    }

    /**
     * 扫荡
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_Battle_Sweep)
    public void sweepReward(Player player, JsonMsg msg){
        int id = msg.getInt("id");
        int type = msg.getInt("type");
        BattleType battleType = BattleType.getBattleType(type);

        BaseBattle battle = player.playerBattle().getPlayerBattle(type);
        AbstractBattleHandler battleHandler = battleType.getBattleHandler();
        // 检查是否可以领取奖励
        if (!battleHandler.isCanSweep(player, battle, id)){
            player.sendErrorMsg(SysConstant.Battle_Sweep_Not);
            return;
        }
        List<Items> rewards = battleHandler.sweep(player, battle, id);
        // 没有奖励
        if (CollUtil.isEmpty(rewards)){
            player.sendErrorMsg(SysConstant.Battle_Sweep_Not);
            return;
        }
        player.notifyObservers(ObservableEnum.BattleSweep, type, id);
        itemBiz.addItem(player, rewards, LogType.BattleSweepReward.value(type));
        JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Player_Battle_Sweep);
        serverMsg.addProperty("rewards", rewards);
        player.sendUserUpdateMsg();
        player.sendMsg(serverMsg);
    }




}
