package com.hm.action.warMake;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.enums.ActivityType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.activity.kfseason.KFSeasonActivity;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wyp
 * @description
 *          玩家战令
 * @date 2021/4/10 20:57
 */
@Action
public class WarMakesAction extends AbstractPlayerAction {
    @Resource
    private WarMakesBiz warMakesBiz;
    @Resource
    private ItemBiz itemBiz;


    @MsgMethod(MessageComm.C2S_War_Makes_Receive)
    public void getReward(Player player, JsonMsg msg) {
        boolean isAll = msg.getBoolean("isAll");
        int id = msg.getInt("id");
        int type = msg.getInt("type");  // 领取类型  WarMakesEnum
        KFSeasonActivity activity = (KFSeasonActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFSeason);
        if(activity == null || !activity.isOpen()) {
            return;
        }
        List<Items> list = Lists.newArrayList();
        if(isAll){
            list = warMakesBiz.receiveAll(player);
        }else{
            list = warMakesBiz.receiveReward(player, type ,id);
        }
        if(CollUtil.isNotEmpty(list)){
            itemBiz.addItem(player, list, LogType.WarMakes.value(activity.getSeasonId()));
            JsonMsg jsonMsg = new JsonMsg(MessageComm.S2C_War_Makes_Receive);
            jsonMsg.addProperty("itemList", list);
            player.sendMsg(jsonMsg);
            player.sendUserUpdateMsg();
        }
    }
}
