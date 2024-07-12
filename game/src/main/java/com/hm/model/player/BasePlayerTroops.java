package com.hm.model.player;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * @Description: 玩家抽象基础列
 * @author siyunlong  
 * @date 2019年1月24日 上午11:25:27 
 * @version V1.0
 */
public class BasePlayerTroops extends PlayerDataContext{
	//世界部队id列表
	private ArrayList<String> troopIdList = Lists.newArrayList();
	
	public void addTroopId(String id) {
		if(!troopIdList.contains(id)) {
			this.troopIdList.add(id);
			SetChanged();
		}
	}
	public void removeTroopId(String id) {
		this.troopIdList.remove(id);
		SetChanged();
	}
	
	public boolean isContain(String id) {
		return this.troopIdList.contains(id);
	}
	
	public int getTroopIndex(String id) {
		if(troopIdList.contains(id)) {
			return troopIdList.indexOf(id)+1;
		}
		return this.troopIdList.size() + 1;
	}

	public ArrayList<String> getTroopIdList() {
		return troopIdList;
	}
	
	public int getTroopSize() {
		return this.troopIdList.size();
	}
}
