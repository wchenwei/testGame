package com.hm.action.activity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hm.db.ActivityUtils;
import com.hm.enums.ActivityType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.ActivityConfItem;
import com.hm.model.activity.ActivityFactory;
import com.hm.model.activity.kfactivity.AbstractKfActivity;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 活动同步
 * @author siyunlong  
 * @date 2019年3月29日 下午9:12:33 
 * @version V1.0
 */
@Slf4j
@Biz
public class ActivityServerConfBiz implements IObserver{
	public static int PreHour = 6;
	

	@Resource
    private ActivityBiz activityBiz;
	

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case HourEvent:
				syncActivity();
				break;
		}
	}
	
	/**
	 * 每小时同步一次活动数据
	 */
	public void syncActivity() {
		Map<String,ActivityConfItem> confMap = queryOpenActivity();
		confMap.putAll(queryShowActivity());
		List<ActivityConfItem> confList = Lists.newArrayList(confMap.values());
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			try {
				syncActivity2Game(confList, serverId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});		
	}
	
	/**
	 * 同步单个服务器
	 * @param serverId
	 */
	public void syncActivity(int serverId) {
		Map<String,ActivityConfItem> confMap = queryOpenActivity();
		confMap.putAll(queryShowActivity());
		List<ActivityConfItem> confList = Lists.newArrayList(confMap.values());
		syncActivity2Game(confList, serverId);	
	}
	
	public static Map<String,ActivityConfItem> queryOpenActivity() {
		MongodDB mongoDb = MongoUtils.getLoginMongodDB();
		Criteria criteria = Criteria
				.where("status").is(1)
				.and("startTime").lt(System.currentTimeMillis())
				.and("endTime").gt(System.currentTimeMillis())
				;
		Query query = new Query(criteria);
		query.limit(Integer.MAX_VALUE);
		List<ActivityConfItem> confList = mongoDb.query(query, ActivityConfItem.class);
		return confList.stream().collect(Collectors.toMap(ActivityConfItem::getId, e -> e));
	}
	
	public static Map<String,ActivityConfItem> queryShowActivity() {
		MongodDB mongoDb = MongoUtils.getLoginMongodDB();
		Criteria criteria = Criteria
				.where("status").is(1)
				.and("viewTime").gt(0L).lt(System.currentTimeMillis()+PreHour*GameConstants.HOUR)
				.and("endTime").gt(System.currentTimeMillis())
				;
		Query query = new Query(criteria);
		query.limit(Integer.MAX_VALUE);
		List<ActivityConfItem> confList = mongoDb.query(query, ActivityConfItem.class);
		return confList.stream().collect(Collectors.toMap(ActivityConfItem::getId, e -> e));
	}
	
	/**
	 * 同步到每个服务器
	 * @param confList
	 * @param serverId
	 */
	private void syncActivity2Game(List<ActivityConfItem> confList,int serverId) {
		ServerInfo serverInfo = ServerUtils.getServerInfo(serverId);
		if(serverInfo == null) {
			return;
		}
		int openDay = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics().getOpenDay();
		if(openDay <= 7) {
			confList = confList.stream().filter(e -> e.getOpenType() == 1).collect(Collectors.toList());
		}
		confList = confList.stream().filter(e -> e.isFitServerInfo(serverInfo)).collect(Collectors.toList());
		if(CollUtil.isEmpty(confList)) {
			return;
		}
		boolean isChange = false;
		//过滤出所有开启活动配置
		for (ActivityConfItem activityConfItem : confList) {
			if(checkActivityCanAdd(activityConfItem, serverId)) {
				isChange = true;
			}
		}
		if(isChange && DateUtil.thisHour(true) > 0) {
			activityBiz.broadPlayerActivityUpdate(serverId);
		}
	}
	
	
	private boolean checkActivityCanAdd(ActivityConfItem entity,int serverId) {
		try {
			ActivityType activityType = ActivityType.getActivityType(entity.getActivityType());
			if(activityType == null) {
				//找不到活动类型
				log.error("找不到活动类型:"+entity.getActivityType());
				return false;
			}
			ActivityItemContainer activityItemContainer = ActivityServerContainer.of(serverId);
//			if(activityItemContainer.activityIsOpen(activityType)) {
//				return false;
//			}
			if(ActivityUtils.getActivity(serverId, entity.getId()) != null) {
				return false;
			}
			log.error(serverId+"加载活动:"+entity.getActivityType());
			AbstractActivity activity = ActivityFactory.createAbstractActivity(activityType,
			         entity.getStartTime(), entity.getEndTime());
			activity.setShowTime(entity.getViewTime());
			activity.setCalTime(new Date(entity.getCalTime()));
			activity.setServerId(serverId);
			activity.setId(entity.getId() + "");
			//检查跨服活动
			if(!checkKfActivity(activity, activityItemContainer)) {
				return false;
			}

			if(StrUtil.isNotBlank(entity.getExtend())){
				activity.loadExtend(entity.getExtend());
			}
			return activityItemContainer.addActivityForDB(activity);
		} catch (Exception e) {
			log.error("checkActivityCanAdd"+serverId, e);
		}
		return false;
	}


	public boolean checkKfActivity(AbstractActivity activity,ActivityItemContainer activityItemContainer) {
		if(activity instanceof AbstractKfActivity) {
			//当前服务器没有分组
			ActivityType activityType = ActivityType.getActivityType(activity.getType());
			if(activityItemContainer.activityIsOpen(activityType)) {
				return false;
			}
//			//不能同时开的跨服战
//			List<ActivityType> kfTypes = Lists.newArrayList(ActivityType.KfSports);
//			if(kfTypes.contains(activityType)) {
//				if(kfTypes.stream().anyMatch(e -> activityItemContainer.activityIsOpen(e))) {
//					return false;
//				}
//			}
			return true;
		}
		return true;
	}
	
	public static void main(String[] args) {
//		Map<String,ActivityConfItem> confMap = queryOpenActivity();
//		confMap.putAll(queryShowActivity());
//		List<ActivityConfItem> confList = Lists.newArrayList(confMap.values());
//		for (ActivityConfItem activityConfItem : confList) {
//			System.err.println(activityConfItem.getId());
//		}
		System.err.println(new DateTime(1561478400000L));
	}
}
