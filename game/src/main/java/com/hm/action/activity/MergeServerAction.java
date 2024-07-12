package com.hm.action.activity;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.ServerMergeConfig;
import com.hm.config.excel.templaextra.ServiceMergeDiscountTemplateImpl;
import com.hm.config.excel.templaextra.ServiceMergeSignTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.LogType;
import com.hm.enums.StatisticsType;
import com.hm.message.MessageComm;
import com.hm.model.activity.mergeserver.MergeServerActivity;
import com.hm.model.activity.mergeserver.MergeServerValue;
import com.hm.model.item.Items;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerCurrency;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 */
@Action
public class MergeServerAction extends AbstractPlayerAction {

    @Resource
    private ServerMergeConfig serverMergeConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private PlayerBiz playerBiz;

    /**
     * 折扣狂欢
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_MS_DiscountBuy)
    public void discountBuy(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        MergeServerValue value = (MergeServerValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.MergeServer);
        if (value == null) {
            return;
        }
        ServiceMergeDiscountTemplateImpl cfg = serverMergeConfig.getDiscountCfg(id);
        if (cfg == null) {
            return;
        }

        Integer buyTimes = value.getDiscountRec().getOrDefault(id, 0);
        // check player level
        int lv = player.playerLevel().getLv();
        if (lv < cfg.getPlayer_level_lower() || lv > cfg.getPlayer_level_upper()) {
            return;
        }

        if (buyTimes >= cfg.getBuy_limit()) {
            return;
        }

        Integer priceNow = cfg.getPrice_now();
        if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, priceNow, LogType.Activity.value(ActivityType.MergeServer.getType()))) {
            return;
        }

        value.incRec(id);
        itemBiz.addItem(player, cfg.getRewardItems(), LogType.Activity.value(ActivityType.MergeServer.getType()));
        player.getPlayerActivity().SetChanged();
        player.sendUserUpdateMsg();
        JsonMsg jsonMsg = JsonMsg.create(MessageComm.S2C_MS_DiscountBuy);
        jsonMsg.addProperty("items", Lists.newArrayList(cfg.getRewardItems()));
        player.sendMsg(jsonMsg);
    }

    @MsgMethod(MessageComm.C2S_MS_Sign)
    public void sign(Player player, JsonMsg msg) {
        // ServiceMergeSignTemplateImpl::getId;
        int id = msg.getInt("id");
        // 0:免费签,1:收费签
        int type = msg.getInt("type");
        MergeServerActivity activity = (MergeServerActivity) ActivityServerContainer.of(player).getAbstractActivity(ActivityType.MergeServer);
        MergeServerValue value = (MergeServerValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.MergeServer);
        ServiceMergeSignTemplateImpl signCfg = serverMergeConfig.getSignCfg(id);
        if (signCfg == null || !signCfg.inRange(value.getPlayerLv())) {
            return;
        }
        // 要签第几天的到
        int day = signCfg.getDay();
        // 超前签到
        if (day > activity.getDays()) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        List<Items> rewardItems;
        // * 签到记录  0: 未签， 1: 免费签到已签 2: 付费已签 3: both
        int rec = value.getSignRec(day - 1);
        if (type == 0) {
            if (rec % 2 == 1) {
                // 该天已经签过
                player.sendErrorMsg(SysConstant.PARAM_ERROR);
                return;
            }
            value.addSignRec(day - 1, 1);
            rewardItems = signCfg.getRewardItems();
        } else if (type == 1) {
            if (day != activity.getDays() || player.getPlayerStatistics().getTodayStatistics(StatisticsType.RECHARGEDAY) < signCfg.getRecharge_gold()) {
                // 直允许签当天的付费签 ,&& 当天必须充值 >= 配置
                player.sendErrorMsg(SysConstant.PARAM_ERROR);
                return;
            }
            // 已签
            if (rec > 1) {
                player.sendErrorMsg(SysConstant.PARAM_ERROR);
                return;
            }
            value.addSignRec(day - 1, 2);
            rewardItems = signCfg.getRewardRechargeItems();
        } else {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        itemBiz.addItem(player, rewardItems, LogType.Activity.value(ActivityType.MergeServer.getType()));
        player.getPlayerActivity().SetChanged();
        player.sendUserUpdateMsg();
        JsonMsg jsonMsg = JsonMsg.create(MessageComm.S2C_MS_Sign);
        jsonMsg.addProperty("rewardItems", rewardItems);
        player.sendMsg(jsonMsg);
    }
}
