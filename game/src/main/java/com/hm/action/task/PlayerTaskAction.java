package com.hm.action.task;


import cn.hutool.core.collection.CollUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.task.biz.MainTaskBiz;
import com.hm.config.TaskConfig;
import com.hm.config.excel.temlate.TaskMainTemplateImpl;
import com.hm.enums.GameTaskType;
import com.hm.enums.LogType;
import com.hm.enums.StatisticsType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.TimeUtils;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.task.MainTask;
import com.hm.model.task.Task;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskStatus;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Action
public class PlayerTaskAction extends AbstractPlayerAction {
    @Resource
    private TaskConfig taskConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private LogBiz logBiz;
    @Resource
    private MainTaskBiz mainTaskBiz;

    /**
     * 领取任务奖励
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Task_Reward)
    public void receiveTask(Player player, JsonMsg msg) {
        int taskId = msg.getInt("taskId"); //任务id
        Task task = player.playerTask().getTaskById(taskId);
        if (task == null) {
            player.sendErrorMsg(SysConstant.Player_Task_Complete_Already);
            return;
        }

        logBiz.addTaskCompleteLog(player, GameTaskType.T4, taskId);
        player.playerTask().finish(taskId);
        List<Items> rewards = taskConfig.getTaskById(taskId).getRewards();
        //领奖
        player.notifyObservers(ObservableEnum.Task_Reward);
        itemBiz.addItem(player, rewards, LogType.TaskReward.value(taskId));
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Task_Reward, rewards);
    }


    /**
     * 主线任务领奖
     */
    @MsgMethod(MessageComm.C2S_Main_Task_Reward)
    public void rewardMainTask(Player player, JsonMsg msg) {
        List<MainTask> mainTaskList = player.playerMainTask().getMainTaskList();

        MainTask task = CollUtil.getFirst(mainTaskList);
        if (task == null || !task.isComplete()) {
            player.sendMsg(SysConstant.PARAM_ERROR);
            return;
        }
        int currentTaskId = task.getId();
        TaskMainTemplateImpl taskMainTemplate = taskConfig.getTaskMainTemplate(currentTaskId);
        if (taskMainTemplate == null) {
            player.sendMsg(SysConstant.PARAM_ERROR);
            return;
        }
        task.setState(TaskStatus.REWARDED);
        player.playerMainTask().removeMainTask(currentTaskId);
        long l = player.getPlayerStatistics().getLifeStatistics(StatisticsType.ONLINE_TIME)
                + TimeUtils.getDifferSecs(player.playerBaseInfo().getLastLoginDate(), new Date());
        // 任务开启到任务奖励领取历时
        long usedSecond = Math.max(l - task.getStartSecond(), 0L);
        player.notifyObservers(ObservableEnum.MainTaskComplete, currentTaskId, usedSecond);

        if (taskConfig.haveNextTask(currentTaskId)) {
            List<TaskMainTemplateImpl> nextTaskList = taskConfig.getNextTaskList(currentTaskId);
            for (TaskMainTemplateImpl template : nextTaskList) {
                mainTaskBiz.addMainTask(player, template);
            }
        }

        List<Items> itemsList = taskMainTemplate.getItemsList();
        itemBiz.addItem(player, itemsList, LogType.TaskReward.value(currentTaskId));
        player.playerMainTask().SetChanged();
        player.sendUserUpdateMsg();

        JsonMsg resultMsg = JsonMsg.create(MessageComm.S2C_Main_Task_Reward);
        resultMsg.addProperty("itemsList", itemsList);
        resultMsg.addProperty("taskId", currentTaskId);
        player.sendMsg(resultMsg);
    }
}
