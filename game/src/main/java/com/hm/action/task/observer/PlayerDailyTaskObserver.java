package com.hm.action.task.observer;

import com.hm.config.TaskConfig;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.model.task.ITaskConfTemplate;
import com.hm.model.task.daily.DailyTask;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskTypeEnum;

import javax.annotation.Resource;
import java.util.List;

/**
 * 每日任务
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/8/11 16:52
 */
@Biz
public class PlayerDailyTaskObserver extends BaseTaskObserver {
    @Resource
    private TaskConfig taskConfig;

    @Override
    public List<ObservableEnum> getObserver() {
        return taskConfig.getDailyTaskObserver();
    }


    @Override
    public void doPlayerTask(Player player, ObservableEnum observableEnum, Object... argv) {
        for (DailyTask dailyTask : player.playerDailyTask().getTaskMap().values()) {
            if (!dailyTask.isOpen()) {
                continue;
            }
            ITaskConfTemplate cfg = taskConfig.getTaskDailyTemplate(dailyTask.getId());
            if (cfg == null || !cfg.isFitObserver(observableEnum)) {
                continue;
            }
            TaskTypeEnum taskTypeEnum = cfg.getTaskTypeEnum();
            if (taskTypeEnum.dealObservableNotice(player, dailyTask, cfg, observableEnum, argv)) {
                player.playerDailyTask().SetChanged();
            }
        }
    }
}
