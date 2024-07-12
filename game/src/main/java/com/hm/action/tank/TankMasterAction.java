package com.hm.action.tank;

import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankMasterBiz;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.TankMasterRewardTemplateImpl;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerTankMaster;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;
import com.hm.war.sg.setting.TankSetting;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * 图鉴
 */
@Action
public class TankMasterAction extends AbstractPlayerAction {
    @Resource
    private TankMasterBiz tankMasterBiz;
    @Resource
    private TankConfig tankConfig;
    @Resource
    private ItemBiz itemBiz;

    @MsgMethod(value = MessageComm.C2S_TankMasterAddScore)
    public void addScore(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Master)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int tankId = msg.getInt("tankId");
        Tank tank = player.playerTank().getTank(tankId);
        if (tank == null){
            player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
            return;
        }
        PlayerTankMaster playerTankMaster = player.playerTankMaster();
        int tankStar = playerTankMaster.getTankStar(tankId);
        if (tankStar >= tank.getStar()){
            player.sendErrorMsg(SysConstant.TANK_STAR_SCORE_LIMIT);
            return;
        }
        TankSetting tankSetting = tankConfig.getTankSetting(tankId);
        int score = tankConfig.getStarMasterScore(tankStar + 1, tankSetting.getRare());

        playerTankMaster.addTankStar(tankId);
        playerTankMaster.addScore(score);
        player.notifyObservers(ObservableEnum.TankMasterScoreAdd, tankId, score);

        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_TankMasterAddScore);
    }

    @MsgMethod(value = MessageComm.C2S_TankMasterScoreReward)
    public void scoreReward(Player player, JsonMsg msg){
        int id = msg.getInt("id");
        PlayerTankMaster playerTankMaster = player.playerTankMaster();
        if (playerTankMaster.isReward(id)){
            player.sendErrorMsg(SysConstant.TANK_STAR_SCORE_REWARD);
            return;
        }
        TankMasterRewardTemplateImpl rewardTemplate = tankConfig.getTankMasterRewardTemplate(id);
        if (rewardTemplate == null){
            return;
        }
        int totalScore = playerTankMaster.getTotalScore();
        if (totalScore >= rewardTemplate.getStar_scores_total()){
            playerTankMaster.addRewards(id);
        }
        List<Items> rewards = rewardTemplate.getRewards();
        itemBiz.addItem(player, rewards, LogType.TankMaster.value(id));
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_TankMasterScoreReward, rewards);
    }
}
