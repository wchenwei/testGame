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

@FileConfig("task_config")
public class TaskTemplate extends TaskConfigTemplate implements ITaskConfTemplate {
	private List<Items> rewards = Lists.newArrayList();
	private TaskTypeEnum taskEnum;

	@ConfigInit
	public void init(){
		rewards = ItemUtils.str2ItemList(this.getTask_reward(), ",", ":");
		taskEnum = TaskTypeEnum.num2enum(this.getTask_type());
	}

	public List<Items> getRewards() {
		return rewards;
	}

	@Override
	public int getTaskFinish() {
		return getFinish_num();
	}


	@Override
	public TaskTypeEnum getTaskTypeEnum() {
		return taskEnum;
	}

	@Override
	public String getTaskFinishPara() {
		return getTask_finish();
	}
}
