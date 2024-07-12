package com.hm.servercontainer.activity;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.GameConstants;
import com.hm.config.excel.ActivityOpenConfig;
import com.hm.db.ActivityUtils;
import com.hm.enums.ActivityType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerMergeData;
import com.hm.servercontainer.ItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Slf4j
public class ActivityItemContainer extends ItemContainer{
	private Map<Integer,AbstractActivity> activityMap = Maps.newConcurrentMap();
	private List<String> runActivityIds = Lists.newArrayList();

	public ActivityItemContainer(int serverId) {
		super(serverId);
	}

	@Override
	public void initContainer() {
		try {
			Map<Integer,AbstractActivity> activityMap = Maps.newConcurrentMap();
			for (AbstractActivity abstractActivity : ActivityUtils.getActivityList(getServerId())) {
				abstractActivity.setServerId(getServerId());
				activityMap.put(abstractActivity.getType(), abstractActivity);
			}
			this.activityMap = activityMap;
			createOpenServerActivity();
			//==========加载永久开启活动==============
			createForverActivity();
			// createFirstServerActivity();
			// createMergeServerActivity();
			reloadRunActivityIds();

			//启动服务器初始化
			for (AbstractActivity activity : this.activityMap.values()) {
				activity.initServerLoad();
			}
		} catch (Exception e) {
			log.error("活动加载出错:"+this.getServerId(), e);
		}
	}

	//初始化永久活动
	private void createForverActivity() {
		for (ActivityType activityType : ActivityType.getForeverActivity()) {
            if (activityMap.containsKey(activityType.getType())) {
                continue;
            }
            if (isManualChangeForever(activityType.getType()) || !ActivityUtils.haveActivity(getServerId(), activityType)) {
                AbstractActivity activity = activityType.getActivityTemplate();
                activity.setServerId(getServerId());
                addActivity(activity);
            }
        }
	}

	/**
	 * 开启开服活动
	 */
	public void createOpenServerActivity() {
		ActivityOpenConfig activityOpenConfig = SpringUtil.getBean(ActivityOpenConfig.class);
		for (AbstractActivity activity : activityOpenConfig.createOpenAcitvity(this)) {
			addActivity(activity);
		}
	}

	/**
	 * 第一次启动服务器时创建获取
	 * ps:用于第一次自动创建，之后需要手动创建的活动
	 * @param serverId
	 */
	private void createFirstServerActivity() {
		List<ActivityType> activityList = Lists.newArrayList();
		for (ActivityType activityType : activityList) {
			//判断是否
			if(!ActivityUtils.haveActivity(getServerId(),activityType) &&
					!activityMap.containsKey(activityType.getType())) {
				AbstractActivity activity = activityType.getActivityTemplate();
				activity.setServerId(getServerId());
				activity.doCreateRepeatActivityForFirst();
				addActivity(activity);
			}
		}
	}

	/**
	 * 添加活动
	 * @param activity
	 * @return true:添加到内存  false:没有添加到内存
	 */
	public boolean addActivity(AbstractActivity activity) {
		if(!activity.checkCanAdd()) {
			return false;
		}
		activity.doCreateActivity();
		ActivityUtils.saveOrUpdate(activity);
		if(!activityMap.containsKey(activity.getType())) {
			this.activityMap.put(activity.getType(), activity);
			return true;
		}
		return false;
	}
	
	public boolean addActivityForDB(AbstractActivity activity) {
		if(!activity.checkCanAdd()) {
			return false;
		}
		activity.doCreateActivity();
		ActivityUtils.saveOrUpdate(activity);
		if(!activity.isOpen()) {
			return false;
		}
		if(!activityMap.containsKey(activity.getType())) {
			this.activityMap.put(activity.getType(), activity);
			return true;
		}
		return false;
	}

	public AbstractActivity getAbstractActivity(ActivityType type) {
		return activityMap.get(type.getType());
	}

	public boolean activityIsOpen(ActivityType type) {
		AbstractActivity activity = getAbstractActivity(type);
		return activity != null && activity.isOpen();
	}

	/**
	 * 判断此时间点,此活动是否是开启状态
	 * @param type
	 * @param tempTime
	 * @return
	 */
	public boolean activityIsOpenForTime(ActivityType type,long tempTime) {
		AbstractActivity activity = getAbstractActivity(type);
		return activity != null && activity.isOpenForTime(tempTime);
	}

	public List<AbstractActivity> getActivityList() {
		return Lists.newArrayList(this.activityMap.values());
	}
	public List<AbstractActivity> getActivityList(Player player) {
		List<AbstractActivity> activityList = getActivityList();
		for (int i = activityList.size()-1; i >= 0; i--) {
			AbstractActivity activity = activityList.get(i);
			if(!checkActivityForPlayer(activity, player)) {
				activityList.remove(i);
			}
		}
		return activityList;
	}
	
	public boolean checkActivityForPlayer(AbstractActivity activity,Player player) {
		try {
			ActivityType activityType = ActivityType.getActivityType(activity.getType());
			if(activityType == null || activity.isCloseForPlayer(player)) {
				return false;
			}
			if(!activityType.isForeverType()){
				boolean isShow = activity.isOpen() || activity.isCanShow();
				if(!isShow) {
					return false;
				}
				player.getPlayerActivity().checkRepeatActivity(activity);
			}
			//检查检查每日活动数据
			activity.checkPlayerActivityValue(player);
			return true;
		} catch (Exception e) {
			log.error("活动加载出错:",e);
		}
		return false;
	}

	public List<String> getRunActivityIds() {
		return Lists.newArrayList(this.runActivityIds);
	}

	private void reloadRunActivityIds() {
		List<String> runActivityIds = Lists.newArrayList();
		for (AbstractActivity activity : activityMap.values()) {
			if(activity.isOpen()) {
				runActivityIds.add(activity.getId());
			}else{
				ActivityType activityType = ActivityType.getActivityType(activity.getType());
				if(activityType != null && !activityType.isForeverType()) {
					activityMap.remove(activity.getType());
				}
			}
		}
		this.runActivityIds = runActivityIds;
	}

//	public boolean closeActivity(String id) {
//		AbstractActivity activity = getActivityById(id);
//		if(activity == null) {
//			return false;
//		}
//		if(activityIsRun(activity)) {
//			this.activityMap.remove(activity.getType());
//		}
//		activity.closeActivity();
//		ActivityUtils.saveOrUpdate(activity);
//		return true;
//	}

	private boolean activityIsCache(AbstractActivity activity) {
		AbstractActivity temp = this.activityMap.get(activity.getType());
		return temp != null && StrUtil.equals(temp.getId(), activity.getId());
	}
	private AbstractActivity getActivityById(String id) {
		for (AbstractActivity activity : this.activityMap.values()) {
			if(StrUtil.equals(id, activity.getId())) {
				return activity;
			}
		}
		return ActivityUtils.getActivity(getServerId(),id);
	}
	public boolean removeActivity(String id) {
		AbstractActivity activity = getActivityById(id);
		if(activity == null) {
			return false;
		}
		ActivityUtils.delete(activity);
		if(activityIsCache(activity)) {
			this.activityMap.remove(activity.getType());
			return true;
		}
		return false;
	}

	/**
	 * 创建开服活动
	 */
	public void createMergeServerActivity() {
		ActivityType activityType = ActivityType.MergeServer;
		if (activityMap.containsKey(activityType.getType())) {
			return;
		}

		ServerMergeData serverMergeData = ServerDataManager.getIntance().getServerData(this.getServerId()).getServerMergeData();
		if (serverMergeData == null) {
			return;
		}

		Date now = new Date();
		// 合服的时间
		Date beginDate = new Date(serverMergeData.getDate());
		long end = DateUtil.beginOfDay(beginDate).getTime() + GameConstants.MergeServerActivityDays * GameConstants.DAY;
		Date endDate = new Date(end);
		if (!DateUtil.isIn(now, beginDate, endDate)) {
			return;
		}

		AbstractActivity activity = activityType.getActivityTemplate();
		activity.setServerId(getServerId());
		activity.doCreateActivity();
		addActivity(activity);
	}
	
	//活动是否为关闭
	public boolean activityIsCloseForNull(int typeId) {
		ActivityType activityType = ActivityType.getActivityType(typeId);
		return activityType == null || getAbstractActivity(activityType) == null;
	}

	public boolean isManualChangeForever(int typeId) {
		return false;
	}

}
