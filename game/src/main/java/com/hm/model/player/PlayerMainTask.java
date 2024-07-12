package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.config.TaskConfig;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.task.MainTask;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家主线任务
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2021/8/11 16:29
 */
@Getter
public class PlayerMainTask extends PlayerDataContext{
    private int lastCompleteId;// 最后一个完成并领取奖励的id
    private ArrayList<MainTask> mainTaskList = Lists.newArrayList();

    public void addMainTask(MainTask mainTask){
        mainTaskList.add(mainTask);
        if (mainTaskList.size() > 1) {
            mainTaskList.sort((o1, o2) -> {
                TaskConfig taskConfig = SpringUtil.getBean(TaskConfig.class);
                int order1 = taskConfig.getTaskMainTemplate(o1.getId()).getOrder();
                int order2 = taskConfig.getTaskMainTemplate(o2.getId()).getOrder();
                return order1 - order2;
            });
        }
        SetChanged();

    }

    public boolean removeMainTask(int taskId){
        boolean b = mainTaskList.removeIf(mainTask -> mainTask.getId() == taskId);
        if (b) {
            this.lastCompleteId = taskId;
            SetChanged();
        }
        return b;
    }
    // public void changeMainTask(MainTask mainTask) {
    //     this.mainTask = mainTask;
    //     SetChanged();
    // }

    @Override
    public void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerMainTask", this);
    }
}
