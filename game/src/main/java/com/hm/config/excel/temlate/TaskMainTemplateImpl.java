package com.hm.config.excel.temlate;


import com.google.common.collect.Lists;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.model.task.ITaskConfTemplate;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskTypeEnum;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

/**
 * @author wyp
 * @description
 *          主线任务
 * @date 2021/8/13 11:37
 */
@Getter
@FileConfig("link_task")
public class TaskMainTemplateImpl extends LinkTaskTemplate implements ITaskConfTemplate {
    private List<Items> itemsList = Lists.newArrayList();


    @ConfigInit
    public void init(){
        this.itemsList = ItemUtils.str2DefaultItemList(getTask_reward());
    }

    @Override
    public int getTaskFinish() {
        return this.getFinish_num();
    }

    @Override
    public String getTaskFinishPara() {
        return getTask_finish();
    }

    @Override
    public boolean isFitObserver(ObservableEnum event) {
        TaskTypeEnum e = getTaskTypeEnum();
        return  e != null && e.isFitObserver(event);
    }

    @Override
    public TaskTypeEnum getTaskTypeEnum() {
        return TaskTypeEnum.num2enum(getTask_type());
    }
}
