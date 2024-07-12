package com.hm.action.task.biz;

import com.hm.config.TaskConfig;
import com.hm.config.excel.temlate.TaskMainTemplateImpl;
import com.hm.enums.StatisticsType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.TimeUtils;
import com.hm.model.player.Player;
import com.hm.model.task.MainTask;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author wyp
 * @description
 *      主线任务
 * @date 2021/8/13 15:45
 */
@Biz
public class MainTaskBiz extends NormalBroadcastAdapter {
    @Resource
    private TaskConfig taskConfig;


    /**
     * 创建玩家时初始化任务
     *
     * @param player
     */
    public void loadTaskForCreatePlayer(Player player) {
        for (TaskMainTemplateImpl template : taskConfig.getCreateMainTaskList()) {
            addMainTask(player, template);
        }
    }

    /**
     * 给玩家添加任务
     * @param player
     * @param template
     */
    public void addMainTask(Player player, TaskMainTemplateImpl template) {
        if(template == null) {
            return;
        }
        MainTask task = this.buildMainTask(player, template);
        template.checkComplete(player, task);
        player.playerMainTask().addMainTask(task);
        player.notifyObservers(ObservableEnum.MainTaskAdd, task.getId());
    }

    /**
     * 构建任务对象
     *
     * @param player
     * @param taskMainTemplate
     * @return
     */
    private MainTask buildMainTask(Player player, TaskMainTemplateImpl taskMainTemplate) {
        long l = player.getPlayerStatistics().getLifeStatistics(StatisticsType.ONLINE_TIME)
                + TimeUtils.getDifferSecs(player.playerBaseInfo().getLastLoginDate(), new Date());
        MainTask mainTask = new MainTask(taskMainTemplate.getId());
        mainTask.setStartSecond(l);
        return mainTask;
    }

}
