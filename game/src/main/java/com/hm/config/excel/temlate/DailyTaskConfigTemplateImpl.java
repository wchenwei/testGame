package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.model.task.ITaskConfTemplate;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskTypeEnum;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-08
 */
@FileConfig("daily_task_config")
public class DailyTaskConfigTemplateImpl extends DailyTaskConfigTemplate implements ITaskConfTemplate {
    private List<Items> rewardLists = Lists.newArrayList();
    private TaskTypeEnum taskType;

    @ConfigInit
    public void init() {
        rewardLists = ItemUtils.str2DefaultItemImmutableList(getTask_reward());
        taskType = TaskTypeEnum.num2enum(getTask_type());
    }

    public List<Items> getRewardLists() {
        return rewardLists;
    }


    public boolean isFitLv(int playerLv) {
        return playerLv >= getLevel_limit();
    }

    @Override
    public int getTaskFinish() {
        return getFinish_num();
    }


    @Override
    public TaskTypeEnum getTaskTypeEnum() {
        return taskType;
    }

    @Override
    public String getTaskFinishPara() {
        return getTask_finish();
    }
}
