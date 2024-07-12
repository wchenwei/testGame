package com.hm.action.task;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.task.biz.RandomTaskBiz;
import com.hm.config.excel.RandomTaskConfig;
import com.hm.enums.LogType;
import com.hm.enums.RandomTaskStatus;
import com.hm.enums.RandomTaskType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.task.Random.BaseRandomTask;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-19
 *
 * @author Administrator
 */
@Action
public class RandomTaskAction extends AbstractPlayerAction {
    @Resource
    private RandomTaskBiz randomTaskBiz;
    @Resource
    private RandomTaskConfig randomTaskConfig;
    @Resource
    private ItemBiz itemBiz;


    @MsgMethod(MessageComm.C2S_RandomTask_Accept)
    public void accept(Player player, JsonMsg msg) {
        BaseRandomTask task = player.playerRandomTask().getTask();
        if (task == null) {
            return;
        }
        //判断是否可以接受
        if (!task.isCanAccept()) {
            return;
        }
        //接受
        task.doAccept();
        player.playerRandomTask().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_RandomTask_Accept);
    }


    @MsgMethod(MessageComm.C2S_RandomTask_Reject)
    public void reject(Player player, JsonMsg msg) {
        BaseRandomTask task = player.playerRandomTask().getTask();
        if (task == null) {
            return;
        }
        //判断是可以放弃
        if (!task.isCanReject()) {
            return;
        }
        //放弃
        task.doReject();
        player.playerRandomTask().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_RandomTask_Reject);
    }

    /**
     * 客户端请求刷新
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_RandomTask_Refresh)
    public void refresh(Player player, JsonMsg msg) {
        /*if (!checkTimeValid(player)) {
            player.sendErrorMsg(-1);
            return;
        }*/
        BaseRandomTask task = player.playerRandomTask().getTask();
        //判断是否满足刷新条件
        if (task == null || !task.isCanRefreshHand()) {
            //同步错误，重新发送给客户端倒计时数据
            player.playerRandomTask().SetChanged();
            player.sendUserUpdateMsg();
            return;
        }

        if (randomTaskBiz.refreshTask(player)) {
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_RandomTask_Refresh);
        }
    }


    @MsgMethod(MessageComm.C2S_RandomTask_Finish)
    public void doFinish(Player player, JsonMsg msg) {
        BaseRandomTask task = player.playerRandomTask().getTask();
        if (task == null) {
            return;
        }
        //没有部队在此城池
        if (!randomTaskBiz.haveTroopInCity(player, task.getCityId())) {
            return;
        }
        //结果
        boolean isSuc = task.doFinish(player, msg);
        // 答题类特殊需求.失败后立马刷一个新的答题类任务,无cd,且不变更任务生成的城市
        boolean b = task.getTaskType() == RandomTaskType.RR_9 || task.getTaskType() == RandomTaskType.RR_8;
        if (!isSuc && b) {
            randomTaskBiz.refreshTask(player, task.getTaskType(), task.getCityId());
        } else {
            task.doFinishAfter(player, isSuc);
        }
        player.playerRandomTask().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_RandomTask_Finish);
    }

    @MsgMethod(MessageComm.C2S_RandomTask_Reward)
    public void reward(Player player, JsonMsg msg) {
        BaseRandomTask task = player.playerRandomTask().getTask();
        if (task == null) {
            return;
        }

        if (task.getTaskStatus() != RandomTaskStatus.Finish) {
            return;
        }
        randomTaskBiz.doReward(player);
        List<Items> itemsList = randomTaskConfig.getRandomTaskRewardTemplateImpl(task.getType(), player.playerLevel().getLv());
        if (CollUtil.isNotEmpty(itemsList)) {
            itemBiz.addItem(player, itemsList, LogType.RandomTask);
            player.playerRandomTask().SetChanged();
            player.sendUserUpdateMsg();
            JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_RandomTask_Reward);
            serverMsg.addProperty("itemsList", itemsList);
            player.sendMsg(serverMsg);
        }
    }

    /**
     * 检测是否cd,
     *
     * @param player
     * @return 当前时间有效 return true
     */
    private boolean checkTimeValid(Player player) {
        BaseRandomTask task = player.playerRandomTask().getTask();
        if (task != null) {
            RandomTaskStatus status = task.getTaskStatus();
            // 完成后或放弃后的刷新cd为1-9分钟随机
            if (status == RandomTaskStatus.Wait) {
                long now = System.currentTimeMillis();
                if (now < task.getStateEndTime()) {
                    return false;
                }
            }
        }
        return true;
    }
}
