package com.hm.action.bluevip;

import com.alibaba.fastjson.JSONObject;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.BlueVipConfig;
import com.hm.config.excel.templaextra.ActiveRewardQqdatingTemplateImpl;
import com.hm.config.excel.templaextra.ActiveRewardQqvipTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.BlueVipEnum;
import com.hm.enums.LogType;
import com.hm.enums.QQPrivilegeEnum;
import com.hm.message.MessageComm;
import com.hm.model.activity.qqprivilege.PrivilegeActivity;
import com.hm.model.activity.qqprivilege.PrivilegeValue;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.bluevip.BlueVipUtil;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wyp
 * @description QQ 游戏 蓝钻相关接口
 * @date 2021/6/4 11:17
 */
@Action
public class BlueVipAction extends AbstractPlayerAction {
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private BlueVipConfig blueVipConfig;


    @MsgMethod(MessageComm.C2S_blue_vip_detail)
    public void blueVipDetail(Player player, JsonMsg msg) {
        Map<String, Object> paramMap = msg.getParamMap();
        JSONObject blueVipDetail = BlueVipUtil.getBlueVipDetail(paramMap, player);
        if (blueVipDetail == null) {
            return;
        }
        player.sendMsg(MessageComm.S2C_blue_vip_detail, blueVipDetail);
        if (player.playerBlueVip().Changed()) {
            player.sendUserUpdateMsg();
        }
    }

    @MsgMethod(MessageComm.C2S_blue_vip_reward)
    public void blueVipReward(Player player, JsonMsg msg) {
        // 礼包 Id, 获取礼包信息
        int id = msg.getInt("id");

        ActiveRewardQqvipTemplateImpl template = blueVipConfig.getById(id);
        if (template == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        Map<String, Object> paramMap = msg.getParamMap();
        BlueVipEnum vipEnum = BlueVipEnum.getVipEnum(template.getType());
        if (!vipEnum.checkCanReceive(player, paramMap, template)) {
            return;
        }
        List<Items> rewardItems = template.getRewardItems();
        itemBiz.addItem(player, rewardItems, LogType.Blue_vip.value(id));
        vipEnum.addGift(player, id);
        JsonMsg jsonMsg = new JsonMsg(MessageComm.S2C_blue_vip_reward);
        jsonMsg.addProperty("itemList", rewardItems);
        player.sendUserUpdateMsg();
        player.sendMsg(jsonMsg);
    }

    @MsgMethod(MessageComm.C2S_QQ_Privilege_Reward)
    public void rewardQQPrivilege(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        PrivilegeActivity activity = (PrivilegeActivity) ActivityServerContainer.of(player).getAbstractActivity(ActivityType.QQPrivilege);
        if (activity == null || activity.isCloseForPlayer(player)) {
            player.sendErrorMsg(SysConstant.Activity_Close);
            return;
        }
        //判断活动是否达到领取条件
        PrivilegeValue privilegeValue = (PrivilegeValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.QQPrivilege);
        ActiveRewardQqdatingTemplateImpl privilege = blueVipConfig.getQQPrivilegeById(id);
        if (privilege == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        QQPrivilegeEnum qqPrivilegeEnum = QQPrivilegeEnum.getType(privilege.getType());
        if (qqPrivilegeEnum == null || !qqPrivilegeEnum.checkCanReward(player, privilegeValue, privilege)) {
            player.sendErrorMsg(SysConstant.Activity_ConditionsNot);
            return;
        }
        LogType logType = LogType.ActivityReward.value(activity.getType());
        List<Items> itemList = privilege.getRewardItems();
        //设置为已领取状态
        qqPrivilegeEnum.setRewardState(player, privilegeValue, id);
        if (itemList != null) {
            itemBiz.addItem(player, itemList, logType);
        }
        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_QQ_Privilege_Reward);
        serverMsg.addProperty("itemList", itemList);
        player.sendMsg(serverMsg);
        player.sendUserUpdateMsg();
    }

}
