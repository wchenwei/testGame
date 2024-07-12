package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.LevelEventConfig;
import com.hm.config.excel.temlate.MissionEventWarTemplate;
import com.hm.config.excel.templaextra.LevelEventNewTemplate;
import com.hm.config.excel.templaextra.LevelEventRewardNewTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerEvent extends PlayerDataContext {
	//当前等级事件(全部目标完成的会从列表中删除)
	private ConcurrentHashMap<Integer,LevelEventNew> map = new ConcurrentHashMap<Integer, LevelEventNew>();
	private transient List<Integer> eventIds= Lists.newArrayList();//已经解锁过的等级事件
	public LevelEventNew getLevelEvent(int id){
		return map.get(id);
	}
	//通关
	public void clearance(int type,int id,int star,String result){
		LevelEventNew levelEvent = getLevelEvent(type);
		if(levelEvent==null){
			return;
		}
		levelEvent.clearance(id);
		SetChanged();
	}
	//重置
	public void reset(int type){
		LevelEventNew levelEvent = getLevelEvent(type);
		if(levelEvent==null){
			return;
		}
		levelEvent.reset();
		SetChanged();
	}
	//是否能够领取
	public boolean canReceive(int id) {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		LevelEventRewardNewTemplate template = levelEventConfig.getRewardNew(id);
		if(template==null){
			return false;
		}
		int warId = template.getWar_id();
		LevelEventNew levelEvent = getLevelEvent(warId);
		if(levelEvent==null){
			return false;
		}
		return levelEvent.canReceive(id);
	}
	//设置领取奖励数据
	public void receive(int id) {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		LevelEventRewardNewTemplate template = levelEventConfig.getRewardNew(id);
		int warId = template.getWar_id();
		LevelEventNew levelEvent = getLevelEvent(warId);
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
		for(LevelEventNew event:map.values()){
            MissionEventWarTemplate template = levelEventConfig.getLevelEventWarNew(event.getWarId());
			if(event.isEnd()&&template.getNext_id()>0){
				this.map.remove(event.getWarId());
				this.map.put(template.getNext_id(), new LevelEventNew(template.getNext_id()));
				SetChanged();
			}
		}
	}

	public void unlockEvent(int warId) {
		this.eventIds.add(warId);
		this.map.put(warId, new LevelEventNew(warId));
		SetChanged();
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerEvent", this);
	}
	public boolean isFighted(int id) {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		LevelEventNewTemplate template = levelEventConfig.getLevelEventNew(id);
		int warId = template.getWar_id();
		LevelEventNew event = getLevelEvent(warId);
		if(event==null){
			return false;
		}
		return event.isFighted(id);
	}

    public void clearAll() {
        this.map.clear();
        this.eventIds.clear();
        SetChanged();
    }

}
