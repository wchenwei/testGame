package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import lombok.Getter;

import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 关卡宝箱
 * @date 2024/3/21 9:36
 */
@Getter
public class PlayerMissionBox extends PlayerDataContext{
	private long boxTime = System.currentTimeMillis();//上次宝箱的领取时间
	private long offLineBoxTime;// 离线奖励累计时间
	private transient Map<Integer,Double> itemMap = Maps.newHashMap();

	public void updateBoxTime() {
		this.boxTime = System.currentTimeMillis();
		SetChanged();
	}

	public void setItemMap(Map<Integer, Double> itemMap) {
		this.itemMap = itemMap;
		SetChanged();
	}

	public long getRewardBoxTime() {
		return Math.min(System.currentTimeMillis()-this.boxTime, CommValueConfig.MissionBoxMaxTime);
	}

	public void addOffLineTime(long addTime){
		long maxTime = SpringUtil.getBean(CommValueConfig.class).getCommValue(CommonValueType.OffLineBoxMaxTime) * GameConstants.MINUTE;
		if (offLineBoxTime >= maxTime){
			return;
		}
		this.offLineBoxTime = Math.min(offLineBoxTime + addTime, maxTime);
		SetChanged();
	}
	
	public void clearOffLineTime(){
		this.offLineBoxTime = 0;
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerMissionBox", this);
	}
}
