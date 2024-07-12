package com.hm.config.excel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.temlate.ActiveOpenServiceTemplate;
import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.ActivityFactory;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.servercontainer.activity.ActivityItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 活动开启配置
 * @author siyunlong  
 * @date 2019年6月8日 下午9:02:57 
 * @version V1.0
 */
@Slf4j
@Config
public class ActivityOpenConfig extends ExcleConfig {
	private List<ActiveOpenServiceTemplate> activityList = Lists.newArrayList();
	
	@Override
	public void loadConfig() {
		this.activityList = loadFile();
	}
	private List<ActiveOpenServiceTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(ActiveOpenServiceTemplate.class), new TypeReference<ArrayList<ActiveOpenServiceTemplate>>(){});
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveOpenServiceTemplate.class);
	}
	
	/**
	 * 获取开服配置活动
	 * @return
	 */
	public List<AbstractActivity> createOpenAcitvity(ActivityItemContainer activityItemContainer) {
		int serverId = activityItemContainer.getServerId();
		int openDay = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics().getOpenDay();
		Date openDate = ServerDataManager.getIntance().getServerData(serverId).getServerOpenData().getFirstOpenDate();
		List<AbstractActivity> tempList = Lists.newArrayList();
		long todayTime = DateUtil.beginOfDay(openDate).getTime();
		for (ActiveOpenServiceTemplate template : activityList) {
			try {
				String extend = template.getPara();
				ActivityType activityType = ActivityType.getActivityType(template.getId());
				if(activityType == null || openDay > template.getOpen_day()) {
					continue;
				}
				AbstractActivity oldActivity = activityItemContainer.getAbstractActivity(activityType);
				if(oldActivity != null && !oldActivity.isOverTime()) {
					continue;
				}
				long startTime = todayTime + Math.max(0, template.getOpen_day()-1)*GameConstants.DAY;
				long endTime = startTime + template.getLast_day() * GameConstants.DAY;
				if(template.getLast_day() < 0) {
					endTime = -1;
				}
				AbstractActivity activity = ActivityFactory.createAbstractActivity(activityType,
						startTime, endTime);
				activity.setShowTime(startTime);
				if(endTime > 0) {
					activity.setCalTime(new Date(endTime));
				}
				activity.setServerId(serverId);
				if(StrUtil.isNotEmpty(extend)) {
					activity.loadExtend(extend);
				}
				if(!activity.checkCanAdd()) {
					log.error(serverId+"开服活动不能开启"+activityType);
					continue;
				}
				tempList.add(activity);
			} catch (Exception e) {
				log.error("开服活动错误:"+template.getId(),e);
			}
		}
		return tempList;
	}
}
