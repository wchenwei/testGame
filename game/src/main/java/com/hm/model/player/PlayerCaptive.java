package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.captive.log.AbstractCaptiveLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @Description: 玩家俘虏模块
 * @author siyunlong  
 * @date 2020年7月1日 下午1:40:08 
 * @version V1.0
 */
public class PlayerCaptive extends PlayerDataContext {
	private int lv;
	//上阵的研究员
	private int researcherId;
	//5个位置槽的信息
	private CaptiveSlot[] slots = new CaptiveSlot[5];
	//被关的tank信息
	private ConcurrentHashMap<Integer,BeCaptiveTankInfo> beCloseMap = new ConcurrentHashMap<>();
	//日志
	private transient ArrayList<AbstractCaptiveLog> logList = Lists.newArrayList();
	//研究员到期时间
	private ConcurrentHashMap<Integer,Long> researcherMap = new ConcurrentHashMap<>();
	// 自动研究
	public boolean autoTech;

	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
		SetChanged();
	}

	/**
	 * 获取可以放置俘虏坦克的坦克槽位
	 * @param maxCount
	 * @return
	 */
	public CaptiveSlot findIdleCaptiveSlot(int maxCount) {
		for (int i = 0; i < lv; i++) {
			if(slots[i] == null) {
				slots[i] = new CaptiveSlot();
				return slots[i];
			}
			if(slots[i].getCount() < maxCount && slots[i].isIdleState()) {
				return slots[i];
			}
		}
		return null;
	}
	
	/**
	 * 处理玩家赎回坦克,删除槽位
	 * @param tankId
	 * @param redeemPlayerId
	 */
	public void doRedeemTank(int tankId,long redeemPlayerId) {
		for (int i = 0; i < lv; i++) {
			if(slots[i] != null && slots[i].doRedeemTank(tankId, redeemPlayerId)) {
				SetChanged();
				return;
			}
		}
	}
	
	/**
	 * 获取所有我的被别人俘虏的坦克id列表
	 * @return
	 */
	public List<Integer> getBeCaptiveTank() {
		List<Integer> tankIds = Lists.newArrayList();
		for (Map.Entry<Integer,BeCaptiveTankInfo> entry: beCloseMap.entrySet()) {
			if(!entry.getValue().isFitTime()) {
				beCloseMap.remove(entry.getKey());
				SetChanged();
			}
			tankIds.add(entry.getKey());
		}
		return tankIds;
	}
	/**
	 * 根据坦克id获取被俘虏的坦克信息
	 * @param tankId
	 * @return
	 */
	public BeCaptiveTankInfo getBeCaptiveTank(int tankId) {
		BeCaptiveTankInfo oppoInfo = beCloseMap.get(tankId);
		if(oppoInfo == null) {
			return null;
		}
		if(oppoInfo.isFitTime()) {
			return oppoInfo;
		}
		beCloseMap.remove(tankId);
		SetChanged();
		return null;
	}

	public void addBeCaptiveTank(BeCaptiveTankInfo captiveTankInfo) {
		this.beCloseMap.put(captiveTankInfo.getTankId(), captiveTankInfo);
		SetChanged();
	}

	public void removeBeCaptiveTank(int tankId) {
		this.beCloseMap.remove(tankId);
		SetChanged();
	}

	public void addLog(AbstractCaptiveLog log) {
		if(this.logList.size() >= 15) {
			this.logList.remove(logList.size()-1);
		}
		this.logList.add(0, log);
		SetChanged();
	}

	/**
	 * 是否已拥有研究员
	 * @param id
	 * @return
	 */
	public boolean checkHaveResearcher(int id){
		for (Map.Entry<Integer,Long> entry : researcherMap.entrySet()){
			long endTime = entry.getValue();
			if (endTime != -1 && endTime < System.currentTimeMillis()){
				researcherMap.remove(entry.getKey());
				SetChanged();
			}
		}
		return researcherMap.containsKey(id);
	}

	public ConcurrentHashMap<Integer, BeCaptiveTankInfo> getBeCloseMap() {
		return beCloseMap;
	}

	public CaptiveSlot[] getSlots() {
		return slots;
	}

	public void changeResearcher(int id){
		this.researcherId = id;
		SetChanged();
	}

	public int getResearcherId() {
		return researcherId;
	}

	public void addResearcher(int id, long endTime){
		this.researcherMap.put(id, endTime);
		SetChanged();
	}

	public void changeAutoTech(boolean autoTech){
		this.autoTech = autoTech;
		SetChanged();
	}

	public boolean isAutoTech(){
		return autoTech;
	}

	public CaptiveSlot getCaptiveSlot(int index){
		return slots[index-1];
	}

	public List<AbstractCaptiveLog> getLogList() {
		return logList;
	}

	public void dayReset() {
		for (int i = 0; i < slots.length; i++) {
			if(slots[i] != null) {
				slots[i].setCount(0);
				SetChanged();
			}
		}
	}

	public void clearData() {
		for (int i = 0; i < slots.length; i++) {
			this.slots[i] = null;
		}
		this.beCloseMap.clear();
		this.logList.clear();
		this.autoTech = false;
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerCaptive", this);
	}

}
