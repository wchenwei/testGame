package com.hm.action.task.observer;


import com.hm.config.ActivityTaskConfig;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.model.task.ActivityTask;
import com.hm.model.task.ITaskConfTemplate;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskTypeEnum;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/3/29 20:55
 */
@Biz
public class PlayerActivityTaskObserver extends BaseTaskObserver {
    @Resource
    private ActivityTaskConfig activityTaskConfig;

    @Override
    public List<ObservableEnum> getObserver() {
        return activityTaskConfig.getTaskObserver();
    }

    @Override
    public void doPlayerTask(Player player, ObservableEnum observableEnum, Object... argv) {
        for (ActivityTask activityTask : player.playerActivityTask().getTasks().values()) {
            if (!activityTask.isOpen()) {
                continue;
            }
            ITaskConfTemplate cfg = activityTaskConfig.getTaskTemplate(activityTask.getId());
            if (cfg == null || !cfg.isFitObserver(observableEnum)) {
                continue;
            }
            TaskTypeEnum taskTypeEnum = cfg.getTaskTypeEnum();
            if (taskTypeEnum.dealObservableNotice(player, activityTask, cfg, observableEnum, argv)) {
                player.playerActivityTask().SetChanged();
            }
        }
    }
}
