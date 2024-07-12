package com.hm.action.task.biz;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.cityworld.biz.ResetWorldBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.GameConfig;
import com.hm.config.TaskConfig;
import com.hm.config.excel.DailyTaskConfig;
import com.hm.config.excel.temlate.DailyTaskConfigTemplate;
import com.hm.config.excel.temlate.DailyTaskConfigTemplateImpl;
import com.hm.config.excel.temlate.DailyTaskWeekRechargeTemplate;
import com.hm.config.excel.templaextra.DailyTaskWeekRewardTemplateImpl;
import com.hm.config.excel.templaextra.TaskBoxTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.BuffType;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.PlayerAssetEnum;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.task.daily.DailyTask;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.observer.TaskStatus;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.rits.cloning.Cloner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-08
 */
@Biz
public class DailyTaskBiz implements IObserver {
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private DailyTaskConfig dailyTaskConfig;
    @Resource
    private ActivityBiz activityBiz;
    @Resource
    private MailBiz mailBiz;
    @Resource
	private ResetWorldBiz resetWorldBiz;
    @Resource
    private TaskConfig taskConfig;

    /**
     * 领取每日任务奖励
     *
     * @param player
     * @param taskId
     * @return
     */
    public List<Items> rewardTask(Player player, int taskId) {
        DailyTask task = player.playerDailyTask().getTask(taskId);
        if (task == null || task.getState() != TaskStatus.COMPLETE) {
            return Lists.newArrayList();
        }

        DailyTaskConfigTemplateImpl cfg = dailyTaskConfig.getDailyTaskCfg(taskId);
        if (cfg == null) {
            return Lists.newArrayList();
        }

        List<Items> rewardLists = Cloner.standard().deepClone(cfg.getRewardLists());

        //是否有buff加成
        double addExpeditionValue = player.playerBuffer().getBuffValue(BuffType.DailyTaskExpBuff);
        addExpeditionValue += resetWorldBiz.getDailyTaskExpAdd(player);
        
        final double totalExpAddRate = addExpeditionValue;
        if (totalExpAddRate > 0) {
            rewardLists.stream().filter(DailyTaskBiz::isExpItems).forEach(e -> e.addCountRate(totalExpAddRate));
        }

        itemBiz.addItem(player, rewardLists, LogType.DailyTaskReward);
        task.setState(TaskStatus.REWARDED);
        return rewardLists;
    }

    /**
     * 经验道具
     *
     * @param items
     * @return true if items is add exp
     */
    private static boolean isExpItems(Items items) {
        return items != null && items.getEnumItemType() == ItemType.CURRENCY
                && PlayerAssetEnum.getPlayerAssetEnum(items.getId()) == PlayerAssetEnum.EXP;
    }

    /**
     * 领取宝箱
     *
     * @param player
     * @param position 宝箱索引1,2,3,4...
     * @return
     */
    public List<Items> openBox(Player player, int position) {
        // 已领取box详情
        Set<Integer> boxOpened = player.playerDailyTask().getBoxOpened();
        if (boxOpened.contains(position)) { // 已经领取过了
            return Lists.newArrayList();
        }

        int lv = player.playerCommander().getMilitaryLv();
        // 玩家拥有的点数
        int ownPoint = calcTaskPoint(player);

        // 所有可领取的
        List<Integer> idList = Lists.newArrayList();
        // < = 0 把可领取的全部领取
        if (position > 0) {
            idList.add(position);
        } else {
            idList.addAll(CollUtil.subtract(dailyTaskConfig.getBoxIdSet(), boxOpened));
        }

        List<Items> rewardList = Lists.newArrayList();
        for (Integer pos : idList) {
            TaskBoxTemplateImpl cfg = dailyTaskConfig.getTaskBoxTemplateImpl(lv, pos);
            if (cfg == null) {
                continue;
            }
            Integer needPoint = cfg.getNeed_point();
            if (needPoint > ownPoint) {
                continue;
            }
            rewardList.addAll(cfg.getRewardList());
            boxOpened.add(pos);
        }

        if (!rewardList.isEmpty()) {
            itemBiz.addItem(player, rewardList, LogType.DailyTaskBox);
            player.playerDailyTask().SetChanged();
        }
        return rewardList;
    }

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.DailyTaskComplete, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        // 等级有变化时,检测是否触发新任务解锁
        if (observableEnum == ObservableEnum.PlayerLevelUp) {
            checkDailyTaskLockState(player);
            // 记录任务积分
        } else if (observableEnum == ObservableEnum.DailyTaskComplete) {
            int taskId = Integer.parseInt(argv[0].toString());
            DailyTaskConfigTemplateImpl cfg = dailyTaskConfig.getDailyTaskCfg(taskId);
            if (cfg != null) {
                player.playerDailyTask().incWeekPoint(cfg.getTask_point());
            }
        }
    }

    /**
     * 检测是否触发解锁新任务
     *
     * @param player
     */
    private void checkDailyTaskLockState(Player player) {
        player.playerDailyTask().checkTaskOpenStatus();
    }

    /**
     * 计算每日任务目前所得到的积分
     *
     * @param player
     * @return
     */
    public int calcTaskPoint(BasePlayer player) {
        return player.playerDailyTask().getTaskMap().values().stream().
                filter(task -> task.getState() == TaskStatus.REWARDED).
                map(task -> dailyTaskConfig.getDailyTaskCfg(task.getId())).
                mapToInt(DailyTaskConfigTemplate::getTask_point).sum();
    }


    /**
     * 玩家每日加载每日任务
     *
     * @param player
     */
    public void reloadDaliyTask(Player player) {
        List<DailyTaskConfigTemplateImpl> unlockDailyTask = taskConfig.getUnlockDailyTask(player, player.playerCommander().getMilitaryLv());
        for (DailyTaskConfigTemplateImpl tpl : unlockDailyTask) {
            DailyTask task = player.playerDailyTask().getTask(tpl.getId());
            if (task == null) {
                task = new DailyTask(tpl.getId());
            }
            if (task.isUnlock()) {
                continue;
            }
            task.setUnlock(true);
            tpl.checkComplete(player, task);
            player.playerDailyTask().addTask(task);
        }
    }

    /**
     * 修正week point
     *
     * @param player
     */
    public void checkWeekPoint(BasePlayer player) {
        if (player.playerDailyTask().getWeekPoint() != 0) {
            return;
        }
        player.playerDailyTask().setBeginLv(player.playerCommander().getMilitaryLv());
        player.playerDailyTask().setWeekTime(System.currentTimeMillis());
        int sum = player.playerDailyTask().getTaskMap().values().stream().filter(v -> v.getState() == TaskStatus.REWARDED).
                map(v -> dailyTaskConfig.getDailyTaskCfg(v.getId())).filter(Objects::nonNull).mapToInt(DailyTaskConfigTemplate::getTask_point).sum();

        if (sum > 0) {
            player.playerDailyTask().incWeekPoint(sum);
        }
    }

    /**
     * 适用于0点刷新过每日任务列表后开启带每日任务的活动
     *
     * @param player
     */
    public void checkNewOpenTask(Player player) {
        checkWeekPoint(player);

        ListMultimap<Integer, DailyTaskConfigTemplateImpl> activityTaskMap = dailyTaskConfig.getActivityTaskMap();
        for (int activityId : activityTaskMap.keySet()) {
            ActivityType type = ActivityType.getActivityType(activityId);
            if (type == null) {
                continue;
            }
            AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(type);
            if (acitvityIsOpen(player, activityId) && activity.getDays() == 1
                    && !inDailyTaskList(player, activityId)) {
                for (DailyTaskConfigTemplateImpl temp : activityTaskMap.get(activityId)) {
                    player.playerDailyTask().addTask(dailyTaskConfig, temp.getId());
                }
            }
        }
        // 服务器当天重启过,可能新增任务
        DateTime dateTime = DateUtil.parseDateTime(GameConfig.StartDate);
        if (DateUtil.isSameDay(dateTime, new Date())) {
            reloadDaliyTask(player);
        }
    }

    /**
     * 判断该活动触发开启的每日任务是否加入到每日任务列表里
     *
     * @param player
     * @param activityId
     * @return
     */
    private boolean inDailyTaskList(BasePlayer player, int activityId) {
        ListMultimap<Integer, DailyTaskConfigTemplateImpl> activityTaskMap = dailyTaskConfig.getActivityTaskMap();
        List<DailyTaskConfigTemplateImpl> list = activityTaskMap.get(activityId);

        return list.stream().anyMatch(t -> player.playerDailyTask().getTaskMap().containsKey(t.getId()));
    }

    /**
     * 检查活动任务是否开启
     *
     * @param player
     * @param activityId
     * @return
     */
    public boolean acitvityIsOpen(BasePlayer player, int activityId) {
        if (activityId <= 0) {
            return true;
        }
        ActivityType activityType = ActivityType.getActivityType(activityId);
        if (activityType == null) {
            return false;
        }
        return activityBiz.checkActivityIsOpen(player, activityType);
    }

    /**
     * 监测未领取的每周任务奖励，并领取
     *
     * @param player
     */
    public void rewardCheck(BasePlayer player) {
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
            }
            if (bHigh && r < 2) {
                itemList.addAll(template.getHighItemsList());
            }
        }

        if (CollUtil.isNotEmpty(itemList)) {
            mailBiz.sendSysMail(player, MailConfigEnum.DailyTaskReward, itemList);
        }
    }
    public void doRecharge(Player player, int rechargeGiftId) {
        DailyTaskWeekRechargeTemplate cfg = dailyTaskConfig.getRechargeCfg(rechargeGiftId);
        if (cfg == null) {
            return;
        }

        if (cfg.getActive_points_add() > 0) {
            player.playerDailyTask().incWeekPoint(cfg.getActive_points_add());
        }

        int state = player.playerDailyTask().getRewardUnLockState();
        if (cfg.getType() == 1 && state % 2 == 0) {
            player.playerDailyTask().incRewardUnLockState(cfg.getType());
        } else if (cfg.getType() == 2 && state / 2 == 0) {
            player.playerDailyTask().incRewardUnLockState(cfg.getType());
        }
        JsonMsg returnMsg = new JsonMsg(MessageComm.S2C_Daily_Task_Week_Refresh);
        player.sendMsg(returnMsg);
    }
}
