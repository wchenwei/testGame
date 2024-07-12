package com.hm.model.cityworld;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.enums.CityTroopType;
import com.hm.enums.NpcType;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.cityworld.troop.ClonePlayerTroop;
import com.hm.model.cityworld.troop.NpcCityTroop;
import com.hm.model.cityworld.troop.PlayerCityTroop;
import com.hm.model.worldtroop.WorldTroop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @Description: 城市部队
 * @author siyunlong  
 * @date 2018年11月3日 上午2:51:06 
 * @version V1.0
 */
public class CityTroop extends CityComponent{
	private ArrayList<BaseCityFightTroop> troopList = Lists.newArrayList();
	
	public int getTroopSize() {
		return this.troopList.size();
	}
	
	public List<BaseCityFightTroop> getTroopList() {
		return troopList;
	}
	
	public BaseCityFightTroop getFirstFightTroop() {
		try {
			if(this.troopList.size() > 0) {
				return this.troopList.get(0);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public void addTroopList(List<BaseCityFightTroop> troopList) {
		this.troopList.addAll(troopList);
		SetChanged();
	}
	public synchronized void addTroop(BaseCityFightTroop troop) {
		if(troop == null) {
			return;
		}
		this.troopList.add(troop);
		SetChanged();
	}
	
	public void loadTroopList(List<BaseCityFightTroop> troopIdList) {
		this.troopList = Lists.newArrayList(troopIdList);
		SetChanged();
	}

	public void addTroop(WorldTroop worldTroop) {
		if(hasTroop(worldTroop.getId())) {
			return;
		}
		this.troopList.add(new PlayerCityTroop(worldTroop.getId()));
		SetChanged();
	}
	
	public synchronized boolean removeTroop(String troopId) {
		for (int i = troopList.size()-1; i >= 0; i--) {
			BaseCityFightTroop temp = troopList.get(i);
			if(temp == null) {
				troopList.remove(i);
				SetChanged();
			}else if(StrUtil.equals(troopId, temp.getId())) {
				troopList.remove(i);
				SetChanged();
				return true;
			}
		}
		return false;
	}
	
	public boolean hasTroop(String troopId) {
		for (int i = troopList.size()-1; i >= 0; i--) {
			BaseCityFightTroop temp = troopList.get(i);
			if(temp == null) {
				troopList.remove(i);
				SetChanged();
			}else if(StrUtil.equals(troopId, temp.getId())) {
				return true;
			}
		}
		return false;
//		return this.troopList.stream().anyMatch(e -> StrUtil.equals(troopId, e.getId()));
	}
	
	public BaseCityFightTroop getCityFightTroop(String troopId) {
		return this.troopList.stream().filter(e -> StrUtil.equals(troopId, e.getId())).findFirst().orElse(null);
	}

	public void changeTroopForLast(String troopId) {
		BaseCityFightTroop troop = getCityFightTroop(troopId);
		if(troop != null) {
			this.troopList.remove(troop);
			this.troopList.add(troop);
		}
	}
	
	public void addNpcToTroop(List<BaseCityFightTroop> npcList) {
		this.troopList.addAll(0, npcList);
		SetChanged();
	}
	
	public void removeNpc(NpcType npcType) {
		for (int i = troopList.size()-1; i >= 0; i--) {
			if(troopList.get(i).getTroopType() == CityTroopType.NpcTroop.getType()) {
				NpcCityTroop npcTroop = (NpcCityTroop)troopList.get(i);
				if(npcTroop.getNpcType() == npcType.getType()) {
					troopList.remove(i);
					SetChanged();
				}
			}
		}
	}
	public void removeAllNpc() {
		for (int i = troopList.size()-1; i >= 0; i--) {
			if(troopList.get(i).getTroopType() == CityTroopType.NpcTroop.getType()) {
				troopList.remove(i);
			}
		}
	}

	//获取城池内拥有clone的玩家id
	public Set<Long> getClonePlayerIds() {
		return this.troopList.stream().filter(e -> e.isCloneTroop())
				.map(e -> ((ClonePlayerTroop)e).getPlayerId())
				.collect(Collectors.toSet());
	}
	
	/**
	 * 获取部队在城战中的位置
	 * @param troopId
	 * @return
	 */
	public int getTroopIndex(String troopId) {
		for (int i = 0; i < troopList.size(); i++) {
			if(StrUtil.equals(troopId, troopList.get(i).getId())) {
				return i;
			}
		}
		return 0;
	}
	
	//获取npc数量
	public long getNpcSize() {
		return this.troopList.stream().filter(e -> e.getTroopType() == CityTroopType.NpcTroop.getType()).count();
	}
	
	public void clearTroop() {
		this.troopList.clear();
		SetChanged();
	}
}
