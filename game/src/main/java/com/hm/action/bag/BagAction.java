package com.hm.action.bag;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.bag.service.AbstractBagService;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.SubItemType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

@Action
public class BagAction extends AbstractPlayerAction {
    @Resource
    private BagBiz bagBiz;
    @Resource
    private ItemConfig itemConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private PlayerBiz playerBiz;
    /**
     * 使用道具
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Bag_UseItem)
    public void userItem(Player player, JsonMsg msg) {
        int itemId = msg.getInt("itemId");
        int count = msg.getInt("count");
        int index = 0;//可选礼包的位置
        boolean isBuy = msg.getBoolean("isBuy");
        count = Math.max(count, 1);
        ItemTemplate itemTemplate = itemConfig.getItemTemplateById(itemId);
        if (itemTemplate == null) {
            player.sendErrorMsg(SysConstant.ITEM_HAVENOT);
            return;
        }
        if (!checkCanUse(player, itemTemplate,count)) {
            player.sendErrorMsg(SysConstant.ITEM_NOT_USE);
            return;
        }
        if(!checkCanUseByTime(itemTemplate)){
            player.sendErrorMsg(SysConstant.ITEM_NOT_USE_BY_TIME);
            return;
        }
        if(itemTemplate.getPlayer_lv() > player.playerLevel().getLv()) {
        	player.sendErrorMsg(SysConstant.Player_Level_Not_Enough);
            return;
        }
        if(isBuy) {//购买并使用
        	
    	}else{//已经消耗了
            boolean canSpend = bagBiz.spendItem(player, itemId, count, LogType.Bag_Used);
            if (!canSpend) {
                player.sendErrorMsg(SysConstant.ITEM_HAVENOT);
                return;
            }
    	}
        SubItemType itemType = itemTemplate.getItemType();
        if(itemType==SubItemType.CHOICEBOX){
        	index = msg.getInt("index");
        }
        List<Items> itemList = bagBiz.rewardBagItem(player, itemTemplate, count,index);
        player.sendUserUpdateMsg();
        //发送客户端道具列表展示
        if (itemList != null) {
            JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_Open_Gift);
            showMsg.addProperty("itemList", itemList);
            player.sendMsg(showMsg);
        }else{
        	JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_Bag_UseItem_Suc);
        	showMsg.addProperty("itemId", itemId);
        	showMsg.addProperty("count", count);
            player.sendMsg(showMsg);
        }
    }
    //装备碎片合成装备
    @MsgMethod(MessageComm.S2C_Player_PartsCompose)
    public void partsCompose(Player player, JsonMsg msg) {
    	//TODO 根据装备碎片id，获取能合成的装备。以及需要的数量
    	int itemId = msg.getInt("itemId");
        int count = msg.getInt("count");
    }

    /**
     * 卖出道具
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_SellItem)
    public void sellItem(Player player, JsonMsg msg) {
        int itemId = msg.getInt("itemId");
        int count = msg.getInt("count");
        int type = msg.getInt("type");
        count = Math.max(1, count);
        ItemType itemType = ItemType.getType(type);
        AbstractBagService bagService = itemType.createAbstractBagService();
        if (!bagService.isCanSell(itemId)) {
            return;
        }
        if(!itemBiz.checkItemEnoughAndSpend(player, itemId,count,ItemType.getType(type), LogType.BagSell)){
        	player.sendErrorMsg(SysConstant.ITEM_HAVENOT);
        	return;
        }
        List<Items> rewards = bagService.sellReward(itemId, count);
        itemBiz.addItem(player, rewards, LogType.BagSell);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_SellItem,rewards);
    }

    private boolean checkCanUse(Player player, ItemTemplate itemTemplate,int count) {
        if (!itemTemplate.isCanBagUse()) {
            return false;
        }
        return true;
    }

    private boolean checkCanUseByTime(ItemTemplate itemTemplate){
        //有效使用时间
        if (!itemTemplate.isEffectiveDate()){
            return false;
        }
        return true;
    }


}
