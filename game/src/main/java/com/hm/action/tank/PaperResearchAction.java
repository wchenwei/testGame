package com.hm.action.tank;

import com.hm.action.AbstractPlayerAction;
import com.hm.action.bag.BagBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.tank.biz.PaperResearchBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.TankLevelTemplate;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.setting.TankSetting;

import javax.annotation.Resource;


@Action
public class PaperResearchAction extends AbstractPlayerAction {

    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private TankConfig tankConfig;
    @Resource
    private BagBiz bagBiz;
    @Resource
    private PaperResearchBiz paperResearchBiz;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private PlayerBiz playerBiz;

    /**
     * 战兽驯化
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Tank_Research)
    public void tankResearch(Player player, JsonMsg msg){
        int tankId = msg.getInt("tankId");
        Tank tank = player.playerTank().getTank(tankId);
        if (tank == null){
            player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
            return;
        }
        if(paperResearchBiz.isResearchMax(player, tank)){
            //没有剩余次数
            player.sendErrorMsg(SysConstant.TankResearch_Not_Enough);
            return;
        }
        TankSetting tankSetting = tankConfig.getTankSetting(tankId);

        int needItemId = commValueConfig.getRareCostItemId(tankSetting.getRare());
        //检查消耗道具
        if(!bagBiz.spendItem(player, needItemId, 1, LogType.TankResearch.value(tankId))) {
            //研发许可不足
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }

        Items reward = paperResearchBiz.tankResearch(player, tankId,1);
        //减去坦克研发剩余次数
        player.playerPaperResearch().tankResearch(tankId);
        //减去坦克研发剩余次数
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Tank_Research, reward);
    }

}
