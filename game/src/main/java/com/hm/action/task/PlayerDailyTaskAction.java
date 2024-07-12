package com.hm.action.task;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.task.biz.DailyTaskBiz;
import com.hm.config.excel.DailyTaskConfig;
import com.hm.config.excel.temlate.DailyTaskConfigTemplateImpl;
import com.hm.config.excel.templaextra.DailyTaskWeekRewardTemplateImpl;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.task.daily.DailyTask;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-08
 */
@Action
public class PlayerDailyTaskAction extends AbstractPlayerAction {
    @Resource
    private DailyTaskBiz dailyTaskBiz;
    @Resource
    private DailyTaskConfig dailyTaskConfig;
    @Resource
    private ItemBiz itemBiz;

    // 每日任务 领取任务奖励
    @MsgMethod(MessageComm.C2S_Daily_Task_Reward)
    public void rewardTask(Player player, JsonMsg msg) {
        int taskId = msg.getInt("taskId");
        List<Items> items = dailyTaskBiz.rewardTask(player, taskId);
        if (CollUtil.isNotEmpty(items)) {
            player.notifyObservers(ObservableEnum.DailyTaskComplete, taskId);
            player.playerDailyTask().SetChanged();
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_Daily_Task_Reward, items);
        }
    }

    // 每日任务开箱子
    @MsgMethod(MessageComm.C2S_Daily_Task_Box)
    public void openBox(Player player, JsonMsg msg) {
        int pos = msg.getInt("pos");
        List<Items> items = dailyTaskBiz.openBox(player, pos);
        if (CollUtil.isNotEmpty(items)) {
            player.playerDailyTask().SetChanged();

            player.notifyObservers(ObservableEnum.OpenDailyTaskBox, pos);
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_Daily_Task_Box, items);
        }
    }

    @MsgMethod(MessageComm.C2S_Daily_Task_Week_Reward)
    public void getWeekReward(Player player, JsonMsg msg) {
        int weekPoint = player.playerDailyTask().getWeekPoint();
        int lv = player.playerDailyTask().getBeginLv();

        Map<Integer, Integer> rec = player.playerDailyTask().getRewardRec();
        List<Items> itemList = Lists.newArrayList();
        // 是否解锁高级奖励
        boolean bHigh = player.playerDailyTask().isRewardUnlock();
        List<DailyTaskWeekRewardTemplateImpl> rewardList = dailyTaskConfig.getRewardList(weekPoint, lv);
        for (DailyTaskWeekRewardTemplateImpl template : rewardList) {
            // (1:base 2:high 3:both)
            Integer r = rec.getOrDefault(template.getId(), 0);
            if (r % 2 == 0) {
                itemList.addAll(template.getBaseItemsList());
                player.playerDailyTask().addRewardRec(template.getId(), 1);
            }
            if (bHigh && r < 2) {
                itemList.addAll(template.getHighItemsList());
                player.playerDailyTask().addRewardRec(template.getId(), 2);
            }
        }
        if (CollUtil.isNotEmpty(itemList)) {
            List<Items> items = ItemUtils.mergeItemList(itemList);
            itemBiz.addItem(player, items, LogType.DailyTaskReward);
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_Daily_Task_Week_Reward, items);
        }
    }

    /**
     * 日常任务和日常活跃箱子
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Task_Reward_All)
    public void rewardTaskAll(Player player, JsonMsg msg) {
        // 获取所有已完成、未领奖task id
        List<Integer> ids = player.playerDailyTask().getTaskMap().values().stream().filter(DailyTask::isComplete)
                .map(DailyTask::getId).collect(Collectors.toList());

        // 过滤掉活动任务
        List<Integer> normalIds = ids.stream().filter(t -> {
            DailyTaskConfigTemplateImpl cfg = dailyTaskConfig.getDailyTaskCfg(t);
            return cfg != null && cfg.getActive_id() == 0;
        }).collect(Collectors.toList());

        List<Items> rewards = Lists.newArrayList();
        for (Integer taskId : normalIds) {
            List<Items> items = dailyTaskBiz.rewardTask(player, taskId);
            if (CollUtil.isNotEmpty(items)) {
                rewards.addAll(items);
                player.notifyObservers(ObservableEnum.DailyTaskComplete, taskId);
            }
        }
        // 尝试开所有未领取箱子
        List<Items> items = dailyTaskBiz.openBox(player, 0);
        if (CollUtil.isNotEmpty(items)) {
            player.notifyObservers(ObservableEnum.OpenDailyTaskBox, 0);
        }
        rewards.addAll(items);
        if (CollUtil.isNotEmpty(rewards)) {
            player.playerDailyTask().SetChanged();
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_Task_Reward_All, ItemUtils.mergeItemList(rewards));
        }
    }
}
