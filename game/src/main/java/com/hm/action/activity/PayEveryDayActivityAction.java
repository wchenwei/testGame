package com.hm.action.activity;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.templaextra.ActiveDailyRechargePointsTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.payeveryday.PayEveryDayValue;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;

/**
 * Description:
 * User: yang xb
 * Date: 2019-10-14
 */
@Action
public class PayEveryDayActivityAction extends AbstractPlayerAction {
    @Resource
    private RechargeConfig rechargeConfig;

    @Resource
    private ItemBiz itemBiz;

    @MsgMethod(MessageComm.C2S_Activity45_Exchange)
    public void exchange(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        ActiveDailyRechargePointsTemplateImpl cfg = rechargeConfig.getPointCfg(id);
        if (cfg == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }


        AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(ActivityType.BuyEveryDay);
        if(activity == null || activity.isCloseForPlayer(player)) {
            player.sendErrorMsg(SysConstant.Activity_Close);
            return;
        }

        PayEveryDayValue value = (PayEveryDayValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.BuyEveryDay);
        if (value == null) {
            player.sendErrorMsg(SysConstant.Activity_Close);
            return;
        }

        // 已领取过
        if (value.getExchangeRec().contains(id)) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        if (value.getPoint() < cfg.getPoints()) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        itemBiz.addItem(player, cfg.getItemList(), LogType.Activity.value(ActivityType.BuyEveryDay.getType()));
        value.getExchangeRec().add(id);
        player.getPlayerActivity().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Activity45_Exchange, cfg.getItemList());
    }
}
