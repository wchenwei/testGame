package com.hm.model.player;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.PlayerActivityValue;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 玩家活动数据
 * @author siyunlong  
 * @date 2018年5月14日 下午3:04:57 
 * @version V1.0
 */
public class PlayerActivity extends PlayerDataContext{
	private ConcurrentHashMap<Integer,PlayerActivityValue> activityMap = new ConcurrentHashMap<>();
	private transient HashSet<Integer> closeActivityTypes = new HashSet<>();

	//每日重置
	public void doActivityClose(ActivityItemContainer activityItemContainer) {
		//每日检查活动是否重置
		for (Map.Entry<Integer,PlayerActivityValue> entry : activityMap.entrySet()) {
			PlayerActivityValue playerActivityValue = entry.getValue();
			try {
				if(playerActivityValue.doActivityClose(super.Context()) || activityItemContainer.activityIsCloseForNull(entry.getKey())) {
					ActivityType activityType = ActivityType.getActivityType(entry.getKey());
					if(activityType != null && activityType.isForeverType()) {
						closeActivityTypes.add(entry.getKey());
					}
					activityMap.remove(entry.getKey());
					SetChanged();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取玩家永久活动数据
	 * @param acitivty
	 * @return
	 */
	private PlayerActivityValue getForverPlayerActivityValue(ActivityType activityType) {
		PlayerActivityValue tempValue = activityMap.get(activityType.getType());
		if(tempValue == null) {
			tempValue = activityType.getPlayerActivityValue();
			activityMap.put(activityType.getType(), tempValue);
			SetChanged();
		}
		return tempValue;
	}
	
	public void checkRepeatActivity(AbstractActivity activity) {
		//活动结束时间 -1代表永久开放,永久开放活动不处理重复开启活动的上期数据
		if (activity != null && activity.getEndTime() == -1) {
			return;
		}
		PlayerActivityValue tempValue = activityMap.get(activity.getType());
		if(tempValue != null && !StrUtil.equals(tempValue.getActivityId(), activity.getId())) {
			try {
				tempValue.doActivityClose(super.Context());
			} catch (Exception e) {
				e.printStackTrace();
			}
			tempValue.clearRepeatValue();
			tempValue.setActivityId(activity.getId());
			SetChanged();
		}
	}
	
	/**
	 * 获取玩家可重复开启活动数据
	 * @param activityType
	 * @param activityId
	 * @return
	 */
	private PlayerActivityValue getRepeatPlayerActivityValue(ActivityType activityType,AbstractActivity activity) {
		PlayerActivityValue tempValue = getForverPlayerActivityValue(activityType);
		String activityId = activity.getId();
		if(tempValue.getActivityId() == null) {
			tempValue.setActivityId(activityId);
		}else if(!StrUtil.equals(tempValue.getActivityId(), activityId)) {
			tempValue.setActivityId(activityId);
			SetChanged();
			// 永久活动 || 开了后就不关闭的活动 不执行clear repeat value
			if (null != activity && !activityType.isForeverType() && activity.getEndTime() != -1) {
				tempValue.clearRepeatValue();
			}
		}
		return tempValue;
	}
	
	/**
	 * 获取玩家的单一活动信息
	 * @param activityType
	 * @return
	 */
	public PlayerActivityValue getPlayerActivityValue(ActivityType activityType) {
		if(activityType.isForeverType()) {
			return getForverPlayerActivityValue(activityType);
		}
		AbstractActivity activity = ActivityServerContainer.of(super.Context().getServerId()).getAbstractActivity(activityType);
		if(activity != null) {
			return getRepeatPlayerActivityValue(activityType, activity);
		}
		return getForverPlayerActivityValue(activityType);
	}

	public PlayerActivityValue getActValueOrNull(ActivityType activityType) {
		return this.activityMap.get(activityType.getType());
	}
	
	public List<PlayerActivityValue> getPlayerActivityValueList() {
		return Lists.newArrayList(activityMap.values());
	}
	
	public boolean isUnlockActivity(int activityType) {
		return isCloseActivity(activityType) 
				|| this.activityMap.containsKey(activityType);
	}
	
	public boolean isCloseActivity(int activityType) {
		return this.closeActivityTypes.contains(activityType);
	}
	
	public void removeCloseActivity(int activityType) {
		this.closeActivityTypes.remove(activityType);
		SetChanged();
	}
	
	public ConcurrentHashMap<Integer, PlayerActivityValue> getActivityMap() {
		return activityMap;
	}


	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerActivity", this);
	}

}
