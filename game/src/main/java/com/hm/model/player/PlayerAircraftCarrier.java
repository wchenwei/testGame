package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.excel.templaextra.CvIslandTemplateImpl;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class PlayerAircraftCarrier extends PlayerDataContext {
	private int lv =1; //航母等级
	private int engineLv; //动力舱等级
	private String[] aircrafts = new String[] {"","","","","","","","","","",""};
	//舰岛内容;类型，id
	private Map<Integer, Integer> island = Maps.newConcurrentMap();
	/**
	 * 升级航母
	 */
	public void upLv(){
		this.lv ++;
		SetChanged();
	}

	/**
	 * 升级动力舱
	 */
	public void upEngineLv(){
		this.engineLv++;
		SetChanged();
	}

	/**
	 * 上阵或下阵飞机
	 */
	public void upAircraft(int index, String uid){
		aircrafts[index-1] = uid;
		SetChanged();
	}

	public void addIsland(Map<Integer, Integer> addIsland) {
		if(addIsland.isEmpty()) {
			return;
		}
		addIsland.forEach((key, value)->{
			island.putIfAbsent(key, value);
		});
		SetChanged();
	}
	
	//飞机是否已经上阵
	public boolean isAircraftUp(String uid) {
		return getAircraftList().contains(uid);
	}
	
	public List<String> getAircraftList(){
		return  Arrays.stream(aircrafts).collect(Collectors.toList());
	}

	public int getIsland(int type) {
		return island.getOrDefault(type, 0);
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerAircraftCarrier", this);
	}


	public void islandLvup(CvIslandTemplateImpl nextIsLand) {
		island.put(nextIsLand.getType(), nextIsLand.getId());
		SetChanged();
	}
}
