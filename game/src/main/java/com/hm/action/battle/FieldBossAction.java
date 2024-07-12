package com.hm.action.battle;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.battle.biz.FieldBossBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.DropConfig;
import com.hm.config.excel.FieldBossConfig;
import com.hm.config.excel.templaextra.FieldBossRewardTemplateImpl;
import com.hm.config.excel.templaextra.FieldBossTemplateImpl;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.FieldBossResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Action
public class FieldBossAction extends AbstractPlayerAction {

    @Resource
    private FieldBossBiz fieldBossBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private FieldBossConfig fieldBossConfig;
    @Resource
    private DropConfig dropConfig;

    /**
     * 兽王试炼战斗
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_FieldBoss_Fight)
    public void fight(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.FieldBoss)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        log.error("兽王试炼开始战斗"+player.getId());
        Date nowTime = new Date();
        int weekDay = DateUtil.getWeek(nowTime);
        // 玩家部队
        List<AbstractFightTroop> playerTroops = TroopServerContainer.of(player).getWorldTroops(player.playerTroops().getTroopIdList())
                .stream().map(WorldTroop::buildPvePlayerTroop).collect(Collectors.toList());
        if (CollUtil.isEmpty(playerTroops)){
            player.sendErrorMsg(SysConstant.Filed_Boos_Not_Troop);
            return;
        }
        int maxNum = commValueConfig.getCommValue(CommonValueType.BeastFightDayNum);
        if (!player.playerFieldBoss().checkAndFight(maxNum)){
            player.sendErrorMsg(SysConstant.Fight_Times_Limit);
            return;
        }
        FieldBossTemplateImpl fieldBossTemplate = fieldBossConfig.getFieldBossTemplate(weekDay);
        Integer index = fieldBossTemplate.getId();// boss位置
        FieldBossRewardTemplateImpl bossRewardTemplate = fieldBossConfig.getFirstFieldBossRewardTemplate();
        // 战斗前排行
        long oldRank = HdLeaderboardsService.getInstance().getRank(player.getServerId(), RankType.FieldBoss.getRankName(nowTime), player.getId()+"");
        FieldBossResult fieldBossResult = fieldBossBiz.doFight(playerTroops, bossRewardTemplate, index);
        long newRank = oldRank;
        if (fieldBossBiz.updatePlayerHurt(player, nowTime, fieldBossResult.getTotalHurt())){
            // 战斗后排行
            newRank = HdLeaderboardsService.getInstance().getRank(player.getServerId(), RankType.FieldBoss.getRankName(nowTime), player.getId()+"");
        }
        player.notifyObservers(ObservableEnum.FightPveBoss, fieldBossResult.getTotalHurt());

        log.error("兽王试炼战斗结束"+player.getId());
        FieldBossRewardTemplateImpl rewardTemplate = fieldBossConfig.getFieldBossRewardTemplate(fieldBossResult.getLastWinBoosId());
        JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Player_FieldBoss_Fight);
        if (rewardTemplate != null){
            List<Items> rewards = dropConfig.randomItem(rewardTemplate.getDropId(index));
            itemBiz.addItem(player, rewards, LogType.FieldBoss.value(rewardTemplate.getId()));
            serverMsg.addProperty("rewards", rewards);
        }
        serverMsg.addProperty("oldRank", oldRank);
        serverMsg.addProperty("newRank", newRank);
        serverMsg.addProperty("fieldBossResult", fieldBossResult);
        player.sendUserUpdateMsg();
        player.sendMsg(serverMsg);

    }

}
