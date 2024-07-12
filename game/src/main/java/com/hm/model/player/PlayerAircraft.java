package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlayerAircraft extends PlayerDataContext {
	@Getter
	private ConcurrentHashMap<String, Aircraft> aircraftMap = new ConcurrentHashMap<>(); //飞机
	private transient double rate = -0.06;//初始获得红色品质飞机的概率 
	private Map<Integer,Integer> lucks = Maps.newConcurrentMap();
	private Map<Integer,Integer> recruitCounts = Maps.newConcurrentMap();
	private Map<Integer,Double> rates = Maps.newConcurrentMap();
	
	//添加飞机
	public void addAircraft(Aircraft aircraft) {
		this.aircraftMap.put(aircraft.getUid(), aircraft);
		SetChanged();
	}
	
	public Aircraft getAircraft(String uid) {
		return aircraftMap.get(uid);
	}
	
	public List<Aircraft> getAircraft(List<String> uids) {
		return uids.stream().map(t ->aircraftMap.get(t)).filter(t ->t!=null).collect(Collectors.toList());
	}
	
	public void removeAircrafts(List<String> uids) {
		uids.forEach(t ->this.aircraftMap.remove(t));
		SetChanged();
	}
	
	public void decomposeAircraft(String uid) {
		this.aircraftMap.remove(uid);
		SetChanged();
	}
	
	public int getLuck(int id) {
		return this.lucks.getOrDefault(id, 0);
	}

	public int getRecruitCount(int id) {
		return this.recruitCounts.getOrDefault(id, 0);
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerAircraft", this);
	}

	public void setLuck(int id,int luck) {
		this.lucks.put(id, luck);
		SetChanged();
	}

	public void setRecruitCount(int id,int recruitCount) {
		this.recruitCounts.put(id, recruitCount);
		SetChanged();
	}

	public double getRate(int id) {
		return rates.getOrDefault(id, -0.06);
	}

	public void setRate(int id,double rate) {
		this.rates.put(id, rate);
		SetChanged();
	}
	
	

	

}
