package com.hm.model.serverpublic;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.TimeUtils;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Setter
public class ServerStatistics extends ServerPublicContext{
	private int serverLv;//当前服务器等级，全服前50名的等级平均值
	private int baseNpcLv;
	private int maxMissionId;//最大解锁管卡
	private int maxPlayerLv;//今日最大玩家等级
	private int openDay = 1;
	private String serverDayMark;//服务器每日标识
	//活动展示坦克id列表
	private ConcurrentHashSet<Integer> activityTankIds = new ConcurrentHashSet<>();
	private ConcurrentHashSet<Integer> activityCarIds = new ConcurrentHashSet<>();
	// 活动解锁特工id
	private ConcurrentHashSet<Integer> activityAgentIds = new ConcurrentHashSet<>();
	private int firstRechargrTankId;//首冲送的坦克ID

	//pve记录最大可领取关卡id
	@Getter
	private Map<Integer,Integer> pveRecordMaxMap = Maps.newHashMap();

	public int getServerLv() {
		return serverLv;
	}

	public void setServerLv(int serverLv) {
		this.serverLv = Math.max(this.serverLv, serverLv);
		SetChanged();
	}

	public int getMaxPlayerLv() {
		//玩家的最大等级肯定大于服务器等级
		return Math.max(this.maxPlayerLv, this.serverLv);
	}

	public void setMaxPlayerLv(int maxPlayerLv) {
		this.maxPlayerLv = Math.max(this.maxPlayerLv, maxPlayerLv);
		SetChanged();
	}

	public void addOpenDay() {
		this.openDay ++;
		SetChanged();
	}

	public int getOpenDay() {
		return openDay;
	}

	public void addActivityTank(Set<Integer> tankIds,Set<Integer> carIds) {
		this.activityTankIds.addAll(tankIds);
		this.activityCarIds.addAll(carIds);
		SetChanged();
	}

	public void addActivityAgent(Collection<Integer> ids) {
		if (activityAgentIds.addAll(ids)) {
			SetChanged();
		}
	}

	public String getServerDayMark() {
		return serverDayMark;
	}

	public void loadServerDayMark() {
		String mark = TimeUtils.formatSimpeTime2(new Date());
		if(!StrUtil.equals(mark, this.serverDayMark)) {
			this.serverDayMark = mark;
			System.err.println("服务器标识:"+this.serverDayMark);
			SetChanged();
			save();
		}
	}

	public void loadFirstRechargrTankId() {
		try {
			CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
			DateTime dateTime = DateUtil.parse(commValueConfig.getStrValue(CommonValueType.FirstRechargTime));
			if(dateTime != null && System.currentTimeMillis() >= dateTime.getTime()) {
				this.firstRechargrTankId = commValueConfig.getCommValue(CommonValueType.FirstRechargTankId);
			}
			System.err.println("服务器首冲坦克ID:"+this.firstRechargrTankId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFirstRechargrTankId(int firstRechargrTankId) {
		this.firstRechargrTankId = firstRechargrTankId;
	}

	public int getFirstRechargrTankId() {
		return firstRechargrTankId;
	}

	public int getBaseNpcLv() {
		return baseNpcLv;
	}

	public void setBaseNpcLv(int baseNpcLv) {
		this.baseNpcLv = baseNpcLv;
		SetChanged();
	}

	public int getMaxMissionId() {
		return maxMissionId;
	}

	public void setMaxMissionId(int maxMissionId) {
		this.maxMissionId = maxMissionId;
		SetChanged();
	}

	public void changePveRecordMax(int type,int id) {
		int tempId = this.pveRecordMaxMap.getOrDefault(type,0);
		if(id > tempId) {
			this.pveRecordMaxMap.put(type,id);
			SetChanged();
			save();
		}
	}

	public int getPveRecordMaxId(int type) {
		return this.pveRecordMaxMap.getOrDefault(type,0);
	}

	public void fillMsg(JsonMsg serverMsg) {
		serverMsg.addProperty("activityTankIds", activityTankIds);
		serverMsg.addProperty("activityCarIds", activityCarIds);
		serverMsg.addProperty("serverLv", serverLv);
		serverMsg.addProperty("maxPlayerLv", maxPlayerLv);
		serverMsg.addProperty("activityAgentIds", activityAgentIds);
		serverMsg.addProperty("firstRechargrTankId", firstRechargrTankId);
		serverMsg.addProperty("pveRecordMap", pveRecordMaxMap);
	}
}
