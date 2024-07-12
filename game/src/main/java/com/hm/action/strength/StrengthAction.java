package com.hm.action.strength;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.strength.StrengthConfig;
import com.hm.config.strength.excel.BlockMapTemplateImpl;
import com.hm.config.strength.excel.BlockPartsTemplate;
import com.hm.enums.*;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerCurrency;
import com.hm.model.strength.StoreVo;
import com.hm.model.strength.StrengthModel;
import com.hm.model.strength.StrengthStore;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

@Action
public class StrengthAction extends AbstractPlayerAction {
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private StrengthBiz strengthBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private StrengthConfig strengthConfig;
    @Resource
    private PlayerBiz playerBiz;


    @MsgMethod(MessageComm.C2S_Strength_Research)
    public void research(Player player, JsonMsg msg) {
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return;
        }
        boolean single = msg.getBoolean("single");
        int num = single ? 1:10;
        if(!player.playerStrength().checkMaxNum(num)){
            // 数量超过限制
            player.sendErrorMsg(SysConstant.STRENGTH_STORE_LIMIT);
            return;
        }
        List<Items> costList = strengthBiz.researchCost(player, single);
        if(CollUtil.isNotEmpty(costList) && !itemBiz.checkItemEnoughAndSpend(player, costList, LogType.Strength)){
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        List<StrengthStore> research = strengthBiz.research(player, num);
        if(CollUtil.isEmpty(costList)){
            // 减少 免费次数
            player.getPlayerCDs().touchCdEvent(CDType.Strength);
        }
        player.playerStrength().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Strength_Research, research);
    }

    @MsgMethod(MessageComm.C2S_Strength_Store_Lock)
    public void lock(Player player, JsonMsg msg) {
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return;
        }
        String uid = msg.getString("uid");
        StrengthStore store = player.playerStrength().getStore(uid);
        if(store == null){
            return;
        }
        store.setLock(msg.getBoolean("lock"));
        player.playerStrength().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Strength_Store_Lock, store);
    }

    @MsgMethod(MessageComm.C2S_Strength_Store_Cost)
    public void upMaxCost(Player player, JsonMsg msg) {
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return;
        }
        String uid = msg.getString("uid");
        StrengthStore store = player.playerStrength().getStore(uid);
        if(store == null){
            return;
        }
        BlockPartsTemplate partsTemplate = strengthConfig.getBlockPartsTemplate(store.getId());
        if(partsTemplate == null){
            return;
        }
        List<StoreVo> list = strengthBiz.maxLvUpCost(player, store, partsTemplate);
        player.sendMsg(MessageComm.S2C_Strength_Store_Cost, list);
    }

    @MsgMethod(MessageComm.C2S_Strength_Store_Lv)
    public void lvUp(Player player, JsonMsg msg) {
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return;
        }
        String uid = msg.getString("uid");
        String costIds = msg.getString("costIds");
        StrengthStore store = player.playerStrength().getStore(uid);
        if(store == null || StrUtil.isBlank(costIds)){
            return;
        }
        BlockPartsTemplate partsTemplate = strengthConfig.getBlockPartsTemplate(store.getId());
        if(partsTemplate == null){
            return;
        }
        // 加经验前等级
        int lv = store.getLv();
        if(lv >= partsTemplate.getLevel_limit()){
            player.sendErrorMsg(SysConstant.LV_MAX);
            return;
        }
        strengthBiz.lvUp(player, store, partsTemplate, costIds);
        player.playerStrength().SetChanged();
        // 等级发生变化
        if(lv != store.getLv()){
            strengthBiz.notifyObserve(player, store, ObservableEnum.StrengthLvChange);
        }
        player.sendUserUpdateMsg();
        JsonMsg jsonMsg = JsonMsg.create(MessageComm.S2C_Strength_Store_Lv);
        jsonMsg.addProperty("store", store);
        jsonMsg.addProperty("lvDiff", store.getLv() - lv);
        player.sendMsg(jsonMsg);
    }

    @MsgMethod(MessageComm.C2S_Strength_Store_Attr)
    public void attr(Player player, JsonMsg msg) {
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return;
        }
        String uid = msg.getString("uid");
        StrengthStore store = player.playerStrength().getStore(uid);
        if(store == null){
            return;
        }
        String costUid = msg.getString("costUid");
        if(StrUtil.equals(uid, costUid)){
            return;
        }
        if(!strengthBiz.attr(player, store, costUid)){
            return;
        }
        player.playerStrength().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Strength_Store_Attr, store);
    }

    @MsgMethod(MessageComm.C2S_Strength_Attr_Confirm)
    public void confirm(Player player, JsonMsg msg) {
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return;
        }
        String uid = msg.getString("uid");
        StrengthStore store = player.playerStrength().getStore(uid);
        if(store == null){
            return;
        }
        // 精炼属性 处理
        strengthBiz.attrUpdate(player, store, msg.getBoolean("update"));
        player.playerStrength().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Strength_Attr_Confirm, store);
    }

    @MsgMethod(MessageComm.C2S_Strength_Store_Sublimation)
    public void sublimation(Player player, JsonMsg msg) {
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return;
        }
        String uid = msg.getString("uid");
        StrengthStore store = player.playerStrength().getStore(uid);
        if(store == null){
            return;
        }
        BlockPartsTemplate partsTemplate = strengthConfig.getBlockPartsTemplate(store.getId());
        // 只有 5星的才可以升华
        if(partsTemplate == null || partsTemplate.getStar() < 5){
            return;
        }
        boolean sublimation = strengthBiz.sublimation(player, store, partsTemplate);
        if(!sublimation){
            return;
        }
        player.playerStrength().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Strength_Store_Sublimation, store);
    }

    @MsgMethod(MessageComm.C2S_Strength_Store)
    public void operation(Player player, JsonMsg msg) {
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return;
        }
        String uid = msg.getString("uid");
        StrengthStore store = player.playerStrength().getStore(uid);
        if(store == null){
            return;
        }
        int modelType = msg.getInt("modelType");
        BlockMapTemplateImpl templateImpl = strengthConfig.getBlockMapTemplateByType(modelType);
        if(templateImpl == null){
            return;
        }
        StrengthModel strengthModel = player.playerStrength().getStrengthModel(modelType);

        if(!strengthBiz.operation(player, store, strengthModel, msg)){
            return;
        }
        player.playerStrength().SetChanged();
        player.notifyObservers(ObservableEnum.StrengthChange, modelType);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Strength_Store, strengthModel);
    }

    @MsgMethod(MessageComm.C2S_Strength_Buy)
    public void buyItem(Player player, JsonMsg msg){
        int num = msg.getInt("num");
        int max = Math.max(1, num);
        long todayStatistics = player.getPlayerStatistics().getTodayStatistics(StatisticsType.Strength_Buy);
        int strengthBuyLimit = commValueConfig.getCommValue(CommonValueType.Strength_Buy);
        if(todayStatistics + max > strengthBuyLimit){
            player.sendErrorMsg(SysConstant.STRENGTH_BUY_LIMIT);
            return;
        }
        int value = commValueConfig.getCommValue(CommonValueType.Strength_Single_Gold_Cost);
        long price = max * value;
        if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, price, LogType.Strength)) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        List<Items> list = commValueConfig.getConvertObj(CommonValueType.Strength_Single_Cost);
        list = ItemUtils.calItemRateReward(list, max);
        itemBiz.addItem(player, list, LogType.Strength);
        player.getPlayerStatistics().addTodayStatistics(StatisticsType.Strength_Buy, max);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Strength_Buy);
    }


}
