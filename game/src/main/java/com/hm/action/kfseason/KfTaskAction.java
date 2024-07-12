package com.hm.action.kfseason;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.kfseason.kftask.PlayerKFTaskBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.KfConfig;
import com.hm.config.excel.templaextra.KfPlayerTaskTemplate;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerKfTask.KFTaskItem;
import com.hm.redis.kftask.BaseKFTask;
import com.hm.redis.kftask.KFPlayerTaskCache;
import com.hm.redis.kftask.KFTaskType;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;

@Action
public class KfTaskAction extends AbstractPlayerAction {
    @Resource
    private PlayerKFTaskBiz playerKFTaskBiz;
    @Resource
    private ItemBiz itemBiz;

    @Resource
    private KfConfig kfConfig;
    @Resource
    private ActivityBiz activityBiz;
    @Resource
    private PlayerBiz playerBiz;

    @MsgMethod(MessageComm.C2S_KFTask_Reward)
    public void getKFTaskReward(Player player, JsonMsg msg) {
        KfPlayerTaskTemplate taskTemplate = kfConfig.getKfPlayerTaskTemplate(msg.getInt("taskId"));
        if (taskTemplate == null) {
            return;
        }
        int id = taskTemplate.getTask_id();
        KFTaskType taskType = KFTaskType.getKFTaskType(taskTemplate.getKf_type());
        boolean isOpen = taskType.toActivityType() == null || activityBiz.checkActivityIsOpen(player, taskType.toActivityType());
        if (!isOpen) {
            player.sendErrorMsg(SysConstant.Activity_Close);
            return;
        }
        KFTaskItem kftaskItem = playerKFTaskBiz.getPlayerKfTaskItem(player, taskType);
        if (kftaskItem.isContainsId(id)) {
            player.sendErrorMsg(SysConstant.Activity_Close);//已经领取了
            return;
        }
        BaseKFTask baseKFTask = KFPlayerTaskCache.getKFPlayerTaskFromDB(taskType, player.getId());
        if (baseKFTask.getEventVal(taskTemplate.getTask_type()) < Integer.parseInt(taskTemplate.getTask_finish())) {
            return;//没有完成
        }
        kftaskItem.addId(id);
        player.playerKfTask().SetChanged();
        int passExp = taskTemplate.getPass_exp();
        playerBiz.addWarMakes(player, passExp, LogType.WarMakes.value(id));
        player.sendUserUpdateMsg();

        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KFTask_Reward);
        serverMsg.addProperty("taskId", id);
        serverMsg.addProperty("passExp", passExp);
        player.sendMsg(serverMsg);
    }

    @MsgMethod(MessageComm.C2S_KFTask_Detail)
    public void getKFTaskDetail(Player player, JsonMsg msg) {
        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KFTask_Detail);
        serverMsg.addProperty("kfTaskList", playerKFTaskBiz.getKfTaskList(player));
        player.sendMsg(serverMsg);
    }

}
