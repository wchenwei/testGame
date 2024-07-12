package com.hm.action.item;

import com.hm.action.AbstractPlayerAction;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;

@Action
public class ItemAction extends AbstractPlayerAction {

    @Resource
    private ItemBiz itemBiz;

    @MsgMethod(MessageComm.C2S_BuyItem)
    public void buyItem(Player player, JsonMsg msg){
        Items buyItem = msg.getObjectFromJson("buyItem", Items.class);// 购买的道具和数量
        Items costItem = msg.getObjectFromJson("costItem", Items.class);// 消耗的单价
        if (buyItem == null || costItem == null){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        if (buyItem.getCount() <= 0 || costItem.getCount() <= 0){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        costItem = ItemUtils.calItemRateReward(costItem, buyItem.getCount());
        if (!itemBiz.checkItemEnoughAndSpend(player, costItem, LogType.BuyItem)){
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        itemBiz.addItem(player, buyItem, LogType.BuyItem);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_BuyItem, buyItem);

    }

}
