package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.mission.vo.MissionResultVo;
import com.hm.model.battle.LastFightResultVo;
import com.hm.model.item.Items;

import java.util.List;

public class PlayerFight extends PlayerDataContext {
	private long fightId;
	private LastFightResultVo lastResult;
	public void createFight() {
		this.fightId = System.currentTimeMillis();
		SetChanged();
	}
	public LastFightResultVo getLastResult() {
		return lastResult;
	}
	
	public MissionResultVo getResult(){
		if(lastResult==null){
			return new MissionResultVo();
		}
		return lastResult.getResult();
	}
	public void doLastResult(LastFightResultVo lastResult) {
		this.lastResult = lastResult;
		SetChanged();
	}
	public long getFightId() {
		return fightId;
	}
	public List<Items> getRewards(){
		if(lastResult==null||lastResult.getResult()==null){
			return Lists.newArrayList();
		}
		return lastResult.getResult().getRewards();
	}
	
	//是否已经结算
	public boolean isCalResult(){
		if(fightId==0){
			return false;
		}
		if(lastResult==null){
			return false;
		}
		return this.fightId==this.lastResult.getFightId();
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerFight", this);
	}
	
}
