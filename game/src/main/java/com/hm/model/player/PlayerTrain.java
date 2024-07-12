package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;

import java.util.List;
import java.util.Map;

public class PlayerTrain extends PlayerBagBase {
	private int curId;//当前训练场
	private int state;
	private Map<Integer,TrainTank> npcMap = Maps.newConcurrentMap();
	List<Integer> clearanceIds = Lists.newArrayList();
	
	public void createNpc(int id,int state,Map<Integer, TrainTank> map) {
		this.curId= id;
		this.state = state;
		this.npcMap = map;
		SetChanged();
	}

	public int getCurId() {
		return curId;
	}

	public int getState() {
		return state;
	}

	public Map<Integer, TrainTank> getNpcMap() {
		return npcMap;
	}
	public void clearen(int id){
		if(!clearanceIds.contains(id)){
			this.clearanceIds.add(id);
			SetChanged();
		}
	}
	
	public void clearTrain() {
		this.curId =0;
		this.npcMap.clear();
		this.state = 0;
		SetChanged();
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerTrain", this);
	}

	public int getNpcNum() {
		return npcMap.size();
	}

	public boolean isCanSweep(int id) {
		return clearanceIds.contains(id);
	}
}
