package com.hm.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.DailyTaskConfigTemplateImpl;
import com.hm.config.excel.temlate.TaskMainTemplateImpl;
import com.hm.config.excel.temlate.TaskTemplate;
import com.hm.libcore.annotation.Config;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Config
public class TaskConfig extends ExcleConfig {
    private Map<Integer, TaskTemplate> taskMap = Maps.newConcurrentMap();
    private List<Integer> initTaskList = Lists.newArrayList();

    //主线、支线、固定任务
    private Map<Integer, TaskMainTemplateImpl> mainTaskMap = Maps.newHashMap();
    private Map<Integer, DailyTaskConfigTemplateImpl> dailyTaskMap = Maps.newConcurrentMap();
    @Getter
    private List<TaskMainTemplateImpl> createMainTaskList;//创建玩家时的任务列表

    @Override
    public void loadConfig() {
        // List<Integer> initTaskList = Lists.newArrayList();
        // Map<Integer, TaskTemplate> tempMap = Maps.newConcurrentMap();
        // List<TaskTemplate> list = JSONUtil.fromJson(getJson(TaskTemplate.class), new TypeReference<List<TaskTemplate>>() {
        // });
        // list.forEach(temp -> {
        //     temp.init();
        //     tempMap.put(temp.getId(), temp);
        //     if (temp.getBase_open() == 1) {//加载初始化任务
        //         initTaskList.add(temp.getId());
        //     }
        // });
        this.taskMap = json2Map(TaskTemplate::getId, TaskTemplate.class);
        List<Integer> collect = taskMap.values().stream().filter(e -> e.getBase_open() == 1).map(TaskTemplate::getId).collect(Collectors.toList());
        this.initTaskList = ImmutableList.copyOf(collect);

        loadMainTask();
        loadDailyTask();
    }

    public void loadMainTask() {
        this.mainTaskMap = json2Map(TaskMainTemplateImpl::getId,TaskMainTemplateImpl.class);
        //初始化任务列表
        this.createMainTaskList = mainTaskMap.values().stream().filter(t->t.getPre_task() == 0).collect(Collectors.toList());
    }

    public void loadDailyTask() {
        this.dailyTaskMap = json2Map(DailyTaskConfigTemplateImpl::getId, DailyTaskConfigTemplateImpl.class);
    }

    public TaskTemplate getTaskById(int taskId) {
        return taskMap.getOrDefault(taskId, null);
    }

    //初始化玩家任务
    public void initPlayer(Player player) {
       initTaskList.forEach(taskId -> player.playerTask().addTask(taskId));
    }

    public boolean isTaskUnlock(BasePlayer player, int taskId) {
        if (taskMap.containsKey(taskId)) {
            TaskTemplate taskTemplate = taskMap.get(taskId);
            return player.playerCommander().getMilitaryLv() >= taskTemplate.getLevel_limit();
        }
        return false;
    }

    public TaskMainTemplateImpl getTaskMainTemplate(int id) {
        return mainTaskMap.get(id);
    }

    public boolean haveNextTask(int currentTaskId) {
        return mainTaskMap.values().stream().anyMatch(e -> e.getPre_task() == currentTaskId);
    }

    public List<TaskMainTemplateImpl> getNextTaskList(int currentTaskId) {
        return mainTaskMap.values().stream().filter(e -> e.getPre_task() == currentTaskId).collect(Collectors.toList());
    }

    public List<ObservableEnum> getMainTaskObserver() {
        return mainTaskMap.values().stream()
                .flatMap(e -> e.getTaskTypeEnum().getObsList().stream()).distinct()
                .collect(Collectors.toList());
    }
    public List<ObservableEnum> getDailyTaskObserver() {
        return dailyTaskMap.values().stream()
                .flatMap(e -> e.getTaskTypeEnum().getObsList().stream()).distinct()
                .collect(Collectors.toList());
    }
    public List<ObservableEnum> getTaskObserver() {
        return taskMap.values().stream()
                .flatMap(e -> e.getTaskTypeEnum().getObsList().stream()).distinct()
                .collect(Collectors.toList());
    }

    public DailyTaskConfigTemplateImpl getTaskDailyTemplate(int id) {
        return dailyTaskMap.get(id);
    }

    public List<DailyTaskConfigTemplateImpl> getUnlockDailyTask(BasePlayer player, int dayPlayerLv) {
        return dailyTaskMap.values().stream()
                .filter(e -> e.isFitLv(dayPlayerLv))
                .collect(Collectors.toList());
    }
}
