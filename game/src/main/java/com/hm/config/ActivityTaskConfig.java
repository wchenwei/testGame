package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.ActivityTaskTemplate;
import com.hm.enums.ActivityType;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.observer.ObservableEnum;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Config
public class ActivityTaskConfig extends ExcleConfig {
    private Map<Integer, ActivityTaskTemplate> taskMap = Maps.newConcurrentMap();
    private ListMultimap<Integer, ActivityTaskTemplate> activityTaskMap = ArrayListMultimap.create();
	@Override
	public void loadConfig() {
		Map<Integer, ActivityTaskTemplate> taskMap = Maps.newConcurrentMap();
		ListMultimap<Integer, ActivityTaskTemplate> activityTaskMapTemp = ArrayListMultimap.create();
		List<ActivityTaskTemplate> list = JSONUtil.fromJson(getJson(ActivityTaskTemplate.class), new TypeReference<List<ActivityTaskTemplate>>() {
		});
		list.forEach(t -> t.init());
		list.forEach(e -> activityTaskMapTemp.put(e.getActive_id(), e));
		this.activityTaskMap = ImmutableListMultimap.copyOf(activityTaskMapTemp);
		taskMap = list.stream().collect(Collectors.toMap(ActivityTaskTemplate::getTask_id, Function.identity()));
		this.taskMap = ImmutableMap.copyOf(taskMap);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActivityTaskTemplate.class);
	}
	
	public ActivityTaskTemplate getTaskTemplate(int id){
		return this.taskMap.get(id);
	}
	
	public List<Integer> getActivityTypes(){
		return this.taskMap.values().stream().map(t ->t.getActive_id()).distinct().collect(Collectors.toList());
	}

	public ListMultimap<Integer, ActivityTaskTemplate> getActivityTaskMap() {
		return activityTaskMap;
	}
	
	public List<ActivityTaskTemplate> getActivityTaskMap(ActivityType actType) {
		return activityTaskMap.get(actType.getType());
	}

	public List<ObservableEnum> getTaskObserver() {
		return activityTaskMap.values().stream()
				.filter(e -> e.getTaskTypeEnum() != null)
				.flatMap(e -> e.getTaskTypeEnum().getObsList().stream())
				.collect(Collectors.toList());
	}
}
