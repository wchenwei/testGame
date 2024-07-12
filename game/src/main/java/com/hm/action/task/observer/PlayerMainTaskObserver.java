package com.hm.action.task.observer;

import com.hm.config.TaskConfig;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.model.task.ITaskConfTemplate;
import com.hm.model.task.MainTask;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskTypeEnum;

import javax.annotation.Resource;
import java.util.List;

/**
 * 主线/支线/阵营任务
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/8/11 16:52
 */
@Biz
public class PlayerMainTaskObserver extends BaseTaskObserver {
    @Resource
    private TaskConfig taskConfig;

    @Override
    public List<ObservableEnum> getObserver() {
        return taskConfig.getMainTaskObserver();
    }

    @Override
    public void doPlayerTask(Player player, ObservableEnum observableEnum, Object... argv) {
        if (player == null) {
            return;
        }
        List<MainTask> mainTaskList = player.playerMainTask().getMainTaskList();
        for (MainTask task : mainTaskList) {
            ITaskConfTemplate cfg = taskConfig.getTaskMainTemplate(task.getId());
            if (cfg == null || !cfg.isFitObserver(observableEnum)) {
                continue;
            }
            TaskTypeEnum taskTypeEnum = cfg.getTaskTypeEnum();
            if (taskTypeEnum.dealObservableNotice(player, task, cfg, observableEnum, argv)) {
                player.playerMainTask().SetChanged();
            }
        }
    }
}
