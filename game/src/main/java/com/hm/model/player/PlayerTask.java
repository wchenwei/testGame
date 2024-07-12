package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.TaskConfig;
import com.hm.config.excel.temlate.TaskTemplate;
import com.hm.model.task.Task;
import com.hm.observer.ObservableEnum;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTask extends PlayerDataContext {
    private ConcurrentHashMap<Integer, Task> taskMap = new ConcurrentHashMap<>();

    public List<Task> getTasks() {
        return Lists.newArrayList(taskMap.values());
    }

    public Task getTaskById(int taskId) {
        return taskMap.getOrDefault(taskId, null);
    }

    //任务结束
    public void finish(int taskId) {
        //先添加下一阶段任务
        addNextTask(taskId);
        //再删除当前任务
        delete(taskId);
    }

    //删除任务
    private void delete(int taskId) {
        taskMap.remove(taskId);
        SetChanged();
    }

    //生成下一阶段任务
    private void addNextTask(int taskId) {
        TaskConfig taskConfig = SpringUtil.getBean(TaskConfig.class);
        TaskTemplate taskTemplate = taskConfig.getTaskById(taskId);
        Integer nextTaskId = taskTemplate.getNext_task();
        if (nextTaskId > 0) {
            addTask(nextTaskId);//添加任务本身
            SetChanged();
        }
    }

    //增加任务
    public void addTask(int taskId) {
        TaskConfig taskConfig = SpringUtil.getBean(TaskConfig.class);
        Player basePlayer = (Player) super.Context();
        Task task = new Task(taskId);
        boolean unlock = taskConfig.isTaskUnlock(basePlayer, taskId);
        task.setUnlock(unlock);
        //判断任务开始时是否完成
        if (task.isUnlock()) {
            TaskTemplate tpl = taskConfig.getTaskById(taskId);
            tpl.checkComplete(basePlayer, task);
        }
        taskMap.put(taskId, task);
        if (task.isComplete()) {
            //触发观察者信号
            basePlayer.notifyObservers(ObservableEnum.TaskComplete, task.getId());
        }
        SetChanged();
    }

    //检查玩家任务是否解锁
    public void checkTaskUnlock() {
        Player player = (Player) super.Context();
        TaskConfig taskConfig = SpringUtil.getBean(TaskConfig.class);
        for (Task task : getTasks()) {
            if (task.isUnlock()) {
                continue;
            }
            boolean unlock = taskConfig.isTaskUnlock(player, task.getId());
            if (!unlock) {
                continue;
            }
            task.setUnlock(true);
            TaskTemplate tpl = taskConfig.getTaskById(task.getId());
            tpl.checkComplete(player, task);
            SetChanged();
        }
    }


    @Override
    public void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerTask", this);
    }
}
