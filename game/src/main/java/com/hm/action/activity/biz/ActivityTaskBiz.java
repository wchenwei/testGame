package com.hm.action.activity.biz;

import com.google.common.collect.ListMultimap;
import com.hm.action.activity.ActivityBiz;
import com.hm.config.ActivityTaskConfig;
import com.hm.config.excel.templaextra.ActivityTaskTemplate;
import com.hm.enums.ActivityType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.task.ActivityTask;
import com.hm.servercontainer.activity.ActivityServerContainer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Biz
public class ActivityTaskBiz{
	@Resource
	private ActivityBiz activityBiz;
	@Resource
	private ActivityTaskConfig activityTaskConfig;
	
	/**
     * 玩家每日加载活动任务
     * @param player
     */
    public void reloadActiviyTask(BasePlayer player) {
    	//先清除关闭了的活动任务
    	clearTask(player);
    	//再将新开的活动任务添加进去并每日刷新的任务重置
    	ListMultimap<Integer, ActivityTaskTemplate> activityTaskMap = activityTaskConfig.getActivityTaskMap();
    	for (int type : activityTaskMap.keySet()) {
    		ActivityType activityType = ActivityType.getActivityType(type);
    		if(activityType!=null){
    			AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
    			int version = activityBiz.getActivityVersion(activity);
    			if(activityBiz.checkActivityIsOpen(player, activityType)) {
    				for (ActivityTaskTemplate temp : activityTaskMap.get(type)) {
    					if(version==temp.getStage()&&(player.playerActivityTask().getTask(temp.getTask_id())==null||temp.getDay_refresh()==1)){
    						player.playerActivityTask().addTask(temp.getTask_id(),activity.getId());
    					}
    				}
    			}
    		}
		}
    }
    
    /**
     * 加载某个活动的任务
     * @param player
     * @param activityType
     */
    public void reloadActiviyTask(BasePlayer player,ActivityType activityType) {
    	ListMultimap<Integer, ActivityTaskTemplate> activityTaskMap = activityTaskConfig.getActivityTaskMap();
    	AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
        if (activity == null) {
            return;
        }
        int version = activityBiz.getActivityVersion(activity);
		if(activityBiz.checkActivityIsOpen(player, activityType)) {
			for (ActivityTaskTemplate temp : activityTaskMap.get(activityType.getType())) {
                if (version == temp.getStage() && player.playerActivityTask().getTask(temp.getTask_id()) == null) {
					//如果没有该任务或该任务是每日刷新的类型则添加一个新的任务进去
					player.playerActivityTask().addTask(temp.getTask_id(),activity.getId());
				}
			}
		}
    }
    
    //清除活动关闭了的任务
    public void clearTask(BasePlayer player){
    	//清除关闭了的活动任务
    	Map<Integer,ActivityTask> tasks = player.playerActivityTask().getTasks();
    	List<Integer> taskIds = tasks.values().stream().filter(t ->{
    		ActivityTaskTemplate template = activityTaskConfig.getTaskTemplate(t.getId());
    		if(template==null){
    			//找不到配置则将任务删除
    			return true;
    		}
    		return !activityBiz.checkActivityIsOpen(player, ActivityType.getActivityType(template.getActive_id()),t.getActivityId());
    	}).map(t ->t.getId()).distinct().collect(Collectors.toList());
    	player.playerActivityTask().deleteTask(taskIds);
    }

    //检查并加载某个活动的任务
    public void checkActivityTask(Player player,ActivityType type) {
    	//活动对玩家没有开放
    	if(!activityBiz.checkActivityIsOpen(player, type)) {
    		return;
    	}
    	//玩家有该活动相关的任务不再处理
    	if(player.playerActivityTask().haveActivityTask(type.getType())) {
    		return;
    	}
    	//重新加载该活动任务
    	reloadActiviyTask(player, type);
    }

}
