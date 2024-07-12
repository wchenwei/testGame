package com.hm.action.giftpack;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.giftpack.bean.GiftPackBean;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.GiftPackageConfig;
import com.hm.config.excel.RechargeActivityConfig;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.temlate.GiftPackageTemplate;
import com.hm.config.excel.templaextra.ActiveGiftDayTemplateImpl;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.PYGiftGroup;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Action
public class GiftAction extends AbstractPlayerAction {
    @Resource
    private RechargeConfig rechargeConfig;
    @Resource
    private GiftPackageConfig giftPackageConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private RechargeActivityConfig rechargeActivityConfig;

    //领取新手礼包
    @MsgMethod(MessageComm.C2S_GetNewPlayerGift)
    public void C2S_GetNewPlayerGift(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        //判断可以领取第几天
        GiftPackBean giftPackBean = player.playerGiftPack().getNewPlayerGiftPack(id);
        if(giftPackBean == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        //购买后count=1
        int index = giftPackBean.getCount();
        //一天只能领取一次
        if(DateUtil.isSameDay(new Date(giftPackBean.getTime()),new Date())) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        RechargeGiftTempImpl temp = rechargeConfig.getRechargeGift(id);
        int[] giftIds = StringUtil.splitStr2IntArray(temp.getExt_info(),",");
        if(index >= giftIds.length) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }


        int giftId = giftIds[index];
        GiftPackageTemplate template = giftPackageConfig.getGiftPackageTemplateById(giftId);
        List<Items> itemsList = template.getItemList();
        itemBiz.addItem(player,itemsList, LogType.NewPlayerGift.value(id+"_"+giftId));

        giftPackBean.setCount(giftPackBean.getCount()+1);
        player.playerGiftPack().SetChanged();
        player.sendUserUpdateMsg();

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_GetNewPlayerGift);
        retMsg.addProperty("id",id);
        retMsg.addProperty("itemsList",itemsList);
        player.sendMsg(retMsg);
    }

    //领取成长基金奖励
    @MsgMethod(MessageComm.C2S_GetGrowFund)
    public void C2S_GetGrowFund(Player player, JsonMsg msg) {
        int type = msg.getInt("type");
        List<Items> itemsList = rechargeActivityConfig.getGrowUpItemList(player,type);
        if(CollUtil.isEmpty(itemsList)) {
            player.sendErrorMsg(SysConstant.Guild_Del_Error);
            return;
        }
        int plevel = type == 1?player.playerMission().getFbId():player.playerCommander().getMilitaryLv();
        itemBiz.addItem(player,itemsList, LogType.GrowUp.value(type+"_"+plevel));

        player.playerGiftPack().SetChanged();
        player.sendUserUpdateMsg();

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_GetGrowFund);
        retMsg.addProperty("type",type);
        retMsg.addProperty("itemsList",itemsList);
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_FreeGift)
    public void C2S_FreeGift(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        ActiveGiftDayTemplateImpl template = giftPackageConfig.getTemplate(id);
        if(!template.isFree()) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        PYGiftGroup giftGroup = player.playerGiftPack().getGiftGroup(template.getGift_type());
        if(giftGroup.getGiftCount(id) > 0) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        if(!template.isFit(giftGroup.getLv())) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        List<Items> itemsList = template.getItemList();
        itemBiz.addItem(player,itemsList, LogType.FreeGift.value(id));
        giftGroup.addGift(id);
        player.playerGiftPack().SetChanged();

        player.sendUserUpdateMsg();

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_FreeGift);
        retMsg.addProperty("id",id);
        retMsg.addProperty("itemsList",itemsList);
        player.sendMsg(retMsg);
    }



}
