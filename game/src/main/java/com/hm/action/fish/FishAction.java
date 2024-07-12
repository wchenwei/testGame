package com.hm.action.fish;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.FishConfig;
import com.hm.config.excel.templaextra.ActiveFishPondTemplateImpl;
import com.hm.config.excel.templaextra.ActiveFishTemplateImpl;
import com.hm.config.excel.templaextra.ActiveGofishLevelTemplateImpl;
import com.hm.db.PlayerUtils;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.chat.InnerChatFacade;
import com.hm.message.MessageComm;
import com.hm.model.fish.FishRecord;
import com.hm.model.fish.FishVO;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @introduce: 钓鱼
 * @author: wyp
 * @DATE: 2023/10/31
 **/
@Action
public class FishAction extends AbstractPlayerAction {
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private InnerChatFacade innerChatFacade;
    @Resource
    private FishConfig fishConfig;
    @Resource
    private FishBiz fishBiz;

    @MsgMethod(MessageComm.C2S_Fish_Record_Pre)
    public void fishPre(Player player, JsonMsg msg) {
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Fish)) {
            return;
        }
        int fishpondId = msg.getInt("fishpondId");
        ActiveGofishLevelTemplateImpl levelTemplate = fishConfig.getGofishLevelById(player.playerFish().getLv());
        if (levelTemplate == null || !levelTemplate.canGoFish(fishpondId)) {
            // 当前等级不能去该鱼池
            return;
        }
        ActiveFishPondTemplateImpl fishPondById = fishConfig.getFishPondById(fishpondId);
        int itemId = msg.getInt("costItemId");
        if (fishPondById == null || !fishPondById.containsItem(itemId)) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            // 鱼池不存在
            return;
        }
        Items items = new Items(itemId, 1, ItemType.ITEM.getType());
        // 校验鱼饵
        if (!itemBiz.checkItemEnough(player, items)) {
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        int randomFish = fishPondById.randomFish(player, itemId);
        player.playerFish().updateFishPre(fishpondId, randomFish);
        player.playerFish().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Fish_Record_Pre, randomFish);
    }


    @MsgMethod(MessageComm.C2S_Fish_Record)
    public void fish(Player player, JsonMsg msg) {
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Fish)){
            return;
        }
        int fishpondId = msg.getInt("fishpondId");
        ActiveGofishLevelTemplateImpl levelTemplate = fishConfig.getGofishLevelById(player.playerFish().getLv());
        if(levelTemplate == null || !levelTemplate.canGoFish(fishpondId)){
            // 当前等级不能去该鱼池
            return;
        }
        ActiveFishPondTemplateImpl fishPondById = fishConfig.getFishPondById(fishpondId);
        int itemId = msg.getInt("costItemId");
        if(fishPondById == null || !fishPondById.containsItem(itemId)){
            // 鱼池不存在
            return;
        }
        Items items = new Items(itemId, 1, ItemType.ITEM.getType());
        // 消耗鱼饵
        if(!itemBiz.checkItemEnoughAndSpend(player, items, LogType.Fish)){
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        JsonMsg jsonMsg = JsonMsg.create(MessageComm.S2C_Fish_Record);
        // 是否成功
        boolean success = msg.getBoolean("success");
        if(success){
            int randomFish = fishPondById.randomFish(player, itemId);
            FishVO fishVO = fishBiz.successFish(player, msg, randomFish);
            jsonMsg.addProperty("fishVO", fishVO);
        }else {
            fishBiz.failFish(player);
        }
        player.playerFish().clearFishPondMap(fishpondId);
        player.sendUserUpdateMsg();
        player.sendMsg(jsonMsg);
    }

    @MsgMethod(MessageComm.C2S_Fish_Reward)
    public void reward(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Fish)){
            return;
        }
        FishRecord fishRecord = player.playerFish().getFishRecord(id);
        if(fishRecord == null || fishRecord.isReward()){
            // 只有首次有奖励
            return;
        }
        ActiveFishTemplateImpl template = fishConfig.getFishTemplateById(id);
        if(template == null){
            return;
        }
        List<Items> firstReward = template.getFirstReward();
        itemBiz.addItem(player, firstReward, LogType.Fish.value(id));
        fishRecord.setReward(true);
        player.playerFish().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Fish_Reward, firstReward);
    }

    @MsgMethod(MessageComm.C2S_Fish_View_Share)
    public void viewShare(Player player,JsonMsg msg){
        int tarPlayerId = msg.getInt("tarPlayerId");
        Player tarPlayer = PlayerUtils.getPlayer(tarPlayerId);
        if(tarPlayer == null || !tarPlayer.getPlayerFunction().isOpenFunction(PlayerFunctionType.Fish)){
            // 观看的玩家没有 解锁该功能
            return;
        }
        player.sendMsg(MessageComm.S2C_Fish_View_Share, tarPlayer.playerFish().getMap());
    }
}
