package com.hm.action.giftpush;

import com.hm.config.GiftPushConfig;
import com.hm.config.excel.templaextra.GiftPushTemplateImpl;
import com.hm.enums.ActionType;
import com.hm.enums.ItemType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.giftpush.PlayerGiftPush;
import com.hm.model.giftpush.PushItem;
import com.hm.model.giftpush.PushItemType;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 限时礼包
 * @date 2024/5/15 15:00
 */
@Biz
public class GiftPushBiz implements IObserver {
    @Resource
    private GiftPushConfig giftPushConfig;
    @Resource
    private LogBiz logBiz;

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);

        for (PushItemType value : PushItemType.values()) {
            ObservableEnum observableEnum = ObservableEnum.getObservableEnum(value.getObserveId());
            ObserverRouter.getInstance().registObserver(observableEnum, this);
        }
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch (observableEnum) {
            case Recharge:
                doRecharge(player, argv);
                break;
            case ItemNotEnough:
                doItemNotEnough(player, argv);
                break;
            default:
                doPush(player, observableEnum.getEnumId(), argv);
                break;
        }

    }

    //检查是否推送限时礼包
    private void doPush(Player player, int enumId, Object[] argv) {
        List<GiftPushTemplateImpl> cfgList = giftPushConfig.getCfgList(enumId);
        checkGiftPushItem(player,cfgList,argv);
    }
    private void doItemNotEnough(Player player, Object[] argv) {
        int type = (int)argv[0];
        int id = (int)argv[1];
        if(type == ItemType.STONE.getType()) {//宝石不区分id
            id = 0;
        }
        String itemId = type+"_"+id;
        List<GiftPushTemplateImpl> cfgList = giftPushConfig.getItemNotList(itemId);
        checkGiftPushItem(player,cfgList,argv);
        if(player.playerGiftPush().Changed()) {
            player.saveDB();
        }
    }

    private void doRecharge(Player player, Object[] argv) {
        int giftId = (int) argv[3];
        GiftPushTemplateImpl giftPushTemplate = giftPushConfig.getCfgByRechargeId(giftId);
        if(giftPushTemplate == null) {
            return;
        }
        int id = giftPushTemplate.getId();

        player.playerGiftPush().doBuyRecharge(giftPushTemplate,giftId);
        // 记录推送涉及到礼包id的购买次数
        logBiz.addPlayerActionLog(player, ActionType.GiftPushBuy.getCode(), String.valueOf(id));
    }



    private void checkGiftPushItem(Player player,List<GiftPushTemplateImpl> cfgList, Object[] argv) {
        PlayerGiftPush playerGiftPush = player.playerGiftPush();

        for (GiftPushTemplateImpl c : cfgList) {
            PushItemType type = c.getType();
            if(!playerGiftPush.havePushItem(c.getId()) && type.isFit(player, c, argv)) {
                PushItem pushItem = new PushItem(c);
                playerGiftPush.addPushItem(pushItem);

                JsonMsg msg = JsonMsg.create(MessageComm.S2C_PushTimeGift);
                msg.addProperty("pushItem",pushItem);
                player.sendMsg(msg);
                logBiz.addPlayerActionLog(player, ActionType.GiftPushOpen.getCode(), String.format("%d-%d", c.getId(), pushItem.getEndTime()));
            }
        }
    }
}
