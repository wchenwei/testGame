package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.action.activity.biz.ActivityTaskBiz;
import com.hm.config.ActivityTaskConfig;
import com.hm.config.excel.templaextra.ActivityTaskTemplate;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.task.ActivityTask;
import com.hm.observer.TaskStatus;

import java.util.List;
import java.util.Map;

public class PlayerActivityTask extends PlayerDataContext {
	private Map<Integer,ActivityTask> tasks = Maps.newConcurrentMap();
	/**
                * 每日重置进度
     */
    public void resetDay() {
    	ActivityTaskBiz dailyTaskBiz = SpringUtil.getBean(ActivityTaskBiz.class);
        dailyTaskBiz.reloadActiviyTask(super.Context());
        SetChanged();
    }
    
    public void addTask(int taskId,String activityId) {
    	ActivityTaskConfig taskConfig = SpringUtil.getBean(ActivityTaskConfig.class);
		Player player = (Player)super.Context();
		ActivityTaskTemplate template = taskConfig.getTaskTemplate(taskId);
		// 根据类型去获取任务
		ActivityTask task = new ActivityTask(taskId);
		task.setActivityId(activityId);
		if(template.isTaskUnlock(player)){
			task.setUnlock(true);
			template.checkComplete(player, task);
		}
		tasks.put(taskId, task);
        SetChanged();
    }

	public Map<Integer,ActivityTask> getTasks() {
		return tasks;
	}
	
	public ActivityTask getTask(int taskId){
		return tasks.get(taskId);
	}

	public void deleteTask(List<Integer> taskIds) {
		taskIds.forEach(t ->this.tasks.remove(t));
		SetChanged();
	}

	public void receive(int id) {
		ActivityTask task = getTask(id);
		if(task==null){
			return;
		}
		task.setState(TaskStatus.REWARDED);
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerActivityTask", this);
	}
	//是否拥有某个活动的任务
	public boolean haveActivityTask(int activityType) {
		ActivityTaskConfig activityTaskConfig = SpringUtil.getBean(ActivityTaskConfig.class);
		return tasks.values().stream().anyMatch(t ->{
					ActivityTaskTemplate template = activityTaskConfig.getTaskTemplate(t.getId());
					if(template==null) {
						return false;
					}
					return template.getActive_id()==activityType;
			   });
	}
}
