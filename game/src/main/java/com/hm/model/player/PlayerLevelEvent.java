package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.LevelEventConfig;
import com.hm.config.excel.temlate.MissionLevelWarTemplate;
import com.hm.config.excel.templaextra.LevelEventRewardTemplate;
import com.hm.config.excel.templaextra.LevelEventTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerLevelEvent extends PlayerDataContext {
	//当前等级事件(全部目标完成的会从列表中删除)
	private ConcurrentHashMap<Integer,LevelEvent> map = new ConcurrentHashMap<Integer, LevelEvent>();
	private transient List<Integer> eventIds= Lists.newArrayList();//已经解锁过的等级事件
	private boolean cal;
	private boolean calNew;
	
	public LevelEvent getLevelEvent(int id){
		return map.get(id);
	}
	//通关
	public void clearance(int type,int id,int star,String result){
		LevelEvent levelEvent = getLevelEvent(type);
		if(levelEvent==null){
			return;
		}
		levelEvent.clearance(id, star, result);
		SetChanged();
	}
	//重置
	public void reset(int type){
		LevelEvent levelEvent = getLevelEvent(type);
		if(levelEvent==null){
			return;
		}
		levelEvent.reset();
		SetChanged();
	}
	//是否能够领取
	public boolean canReceive(int id) {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		LevelEventRewardTemplate template = levelEventConfig.getReward(id);
		if(template==null){
			return false;
		}
		int warId = template.getWar_id();
		LevelEvent levelEvent = getLevelEvent(warId);
		if(levelEvent==null){
			return false;
		}
		return levelEvent.canReceive(id);
	}
	//设置领取奖励数据
	public void receive(int id) {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		LevelEventRewardTemplate template = levelEventConfig.getReward(id);
		int warId = template.getWar_id();
		LevelEvent levelEvent = getLevelEvent(warId);
		if(levelEvent==null){
			return ;
		}
		levelEvent.receive(id);
		SetChanged();
	}
	public boolean isUnlockEvent(int id){
		return this.eventIds.contains(id);
	}
	//检查事件奖励是否已经领完并清除
	public void checkEvent(){
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		for(LevelEvent event:map.values()){
			MissionLevelWarTemplate template = levelEventConfig.getLevelEventWar(event.getWarId());
			if (event.isEndOld() && template.getNext_id() > 0) {
				this.map.remove(event.getWarId());
				this.map.put(template.getNext_id(), new LevelEvent(template.getNext_id()));
				SetChanged();
			}
		}
	}

	public void unlockEvent(int warId) {
		this.eventIds.add(warId);
		this.map.put(warId, new LevelEvent(warId));
		SetChanged();
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerLevelEvent", this);
	}
	public boolean isFighted(int id) {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		LevelEventTemplate template = levelEventConfig.getLevelEvent(id);
		int warId = template.getWar_id();
		LevelEvent event = getLevelEvent(warId);
		if(event==null){
			return false;
		}
		return event.isFighted(id);
	}
	public boolean isCal() {
		return cal;
	}
	
	public List<LevelEvent> getLevelEvents(){
		return Lists.newArrayList(map.values());
	}
	public void setCal(boolean cal) {
		this.cal = cal;
		SetChanged();
	}

	public void setCalNew(boolean cal) {
		this.calNew = cal;
		SetChanged();
	}

	public boolean isCalNew() {
		return calNew;
	}
	
}
