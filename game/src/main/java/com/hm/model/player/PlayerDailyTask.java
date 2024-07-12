package com.hm.model.player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.action.task.biz.DailyTaskBiz;
import com.hm.config.excel.DailyTaskConfig;
import com.hm.config.excel.temlate.DailyTaskConfigTemplateImpl;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.task.daily.DailyTask;
import com.hm.observer.ObservableEnum;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-08
 */
public class PlayerDailyTask extends PlayerDataContext {
    // task id, task;
    @Getter
    private Map<Integer, DailyTask> taskMap = Maps.newHashMap();
    // 已经打开的箱子索引
    private Set<Integer> boxOpened = Sets.newHashSet();
    /**
     * 本周活跃积分
     */
    private int weekPoint;
    /**
     * 时间标记
     */
    private transient long weekTime;
    /**
     * 本周首次生成任务列表时的用户等级
     */
    private int beginLv;
    /**
     * DailyTaskWeekRewardTemplateImpl::getId : (1:base 2:high 3:both)
     */
    private Map<Integer, Integer> rewardRec = Maps.newConcurrentMap();
    /**
     * 0:null,1:高阶任务证书,2:传奇任务证书,3:both
     */
    private int rewardUnLockState;

    /**
     * 每日重置进度
     */
    public void resetDay() {
        Player player = (Player) super.Context();
        DailyTaskBiz dailyTaskBiz = SpringUtil.getBean(DailyTaskBiz.class);
        // 跨周
        if (!DateUtil.isInNowWeek(weekTime)) {
            // 把未领取奖励发邮件
            dailyTaskBiz.rewardCheck(player);
            rewardRec.clear();
            // 记录当前周用户等级
            beginLv = player.playerCommander().getMilitaryLv();
            // 记录时间
            weekTime = System.currentTimeMillis();
            // 清零每周积分
            weekPoint = 0;
            rewardUnLockState = 0;
        }
        taskMap.clear();
        boxOpened.clear();
        dailyTaskBiz.reloadDaliyTask(player);
        SetChanged();
    }

    public void addTask(DailyTaskConfig taskConfig, int taskId) {
        BasePlayer basePlayer = super.Context();
        DailyTask task = new DailyTask(taskId);
        boolean unlock = taskConfig.isTaskUnlock(basePlayer, taskId);
        task.setUnlock(unlock);
        taskMap.put(taskId, task);
        SetChanged();
    }

    public void addTask(DailyTask task) {
        taskMap.put(task.getId(), task);
        SetChanged();
    }

    public DailyTask getTask(int taskId) {
        return taskMap.getOrDefault(taskId, null);
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("dailyTask", this);
    }

    public Map<Integer, DailyTask> getTaskMap() {
        return taskMap;
    }

    public Set<Integer> getBoxOpened() {
        return boxOpened;
    }


    /**
     * 检测是否有等级解锁的
     */
    public void checkTaskOpenStatus() {
        BasePlayer basePlayer = this.Context();
        if (basePlayer == null) {
            return;
        }
        DailyTaskConfig dailyTaskConfig = SpringUtil.getBean(DailyTaskConfig.class);
        for (DailyTask task : taskMap.values()) {
            if (task.isUnlock()) {
                continue;
            }

            DailyTaskConfigTemplateImpl cfg = dailyTaskConfig.getDailyTaskCfg(task.getId());
            if (cfg == null) {
                continue;
            }
            if (cfg.isFitLv(basePlayer.playerCommander().getMilitaryLv())) {
                task.setUnlock(true);
                SetChanged();
            }
        }
    }

    public Map<Integer, Integer> getRewardRec() {
        return rewardRec;
    }

    public void addRewardRec(int id, int num) {
        rewardRec.merge(id, num, Integer::sum);
        SetChanged();
    }

    public void setWeekTime(long weekTime) {
        this.weekTime = weekTime;
        SetChanged();
    }

    public void setBeginLv(int beginLv) {
        this.beginLv = beginLv;
        SetChanged();
    }

    public int getBeginLv() {
        return beginLv;
    }

    public int getWeekPoint() {
        return weekPoint;
    }

    public void incWeekPoint(int inc) {
        weekPoint += inc;
        this.Context().notifyObservers(ObservableEnum.WeekPointAdd, weekPoint);
        this.Context().notifyObservers(ObservableEnum.ActivePointAdd, inc);
        SetChanged();
    }

    public int getRewardUnLockState() {
        return rewardUnLockState;
    }

    public void incRewardUnLockState(int inc) {
        rewardUnLockState += inc;
        SetChanged();
    }

    /**
     * 是否解锁高阶奖励
     *
     * @return
     */
    public boolean isRewardUnlock() {
        return rewardUnLockState > 0;
    }
}
