package com.hm.model.player;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.robsupply.biz.NpcRobSupply;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 玩家掠夺补给信息
 * @author siyunlong  
 * @date 2019年1月24日 上午11:18:31 
 * @version V1.0
 */
public class PlayerRobSupply extends PlayerDataContext{
	//随机城池
	private ArrayList<SupplyItem> supplyList = Lists.newArrayList();
	//玩家自己的坦克
//	private ConcurrentHashMap<Integer, Long> tankMap = new ConcurrentHashMap<>();
	//已经派出去的坦克id列表
	private HashSet<Integer> dispatchTanks = new HashSet<>();
	//战斗报告
	private transient LinkedList<String> recordList = new LinkedList<>();
	//敌方部队列表
	private transient List<String> enemyList = new ArrayList<>();
	//npc列表
	private transient Map<String,NpcRobSupply> npcMap = Maps.newHashMap();

	@Getter
	private boolean freeRefresh;
	
	public void dayReset() {
		freeRefresh = false;
		SetChanged();
//		this.tankMap.clear();
//		SetChanged();
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerRobSupply", this);
	}

	public List<SupplyItem> getSupplyList() {
		return supplyList;
	}
	public void addSupplyItem(List<SupplyItem> newList) {
		this.supplyList.addAll(newList);
		SetChanged();
	}
	
	public SupplyItem getSupplyItem(String id) {
		return this.supplyList.stream().filter(e -> StrUtil.equals(id, e.getId())).findFirst().orElse(null);
	}
	public void removeSupplyItem(String id) {
		for (int i = this.supplyList.size()-1; i >= 0; i--) {
			if(StrUtil.equals(id, this.supplyList.get(i).getId())) {
				this.supplyList.remove(i);
				SetChanged();
			}
		}
	}
	
	public List<String> getTroopIdList() {
		return this.supplyList.stream().map(e -> e.getId()).collect(Collectors.toList());
	}
//	public long getTankHp(int tankId) {
//		return this.tankMap.getOrDefault(tankId, -1L);
//	}
//	public void syncTankHp(List<TankArmy> tankList) {
//		tankList.forEach(tank -> this.tankMap.put(tank.getId(), tank.getHp()));
//		SetChanged();
//	}

	public List<String> getEnemyList() {
		return enemyList;
	}
	public void setEnemyList(List<String> enemyList) {
		this.enemyList = enemyList;
		SetChanged();
	}

	public LinkedList<String> getRecordList() {
		return recordList;
	}
	public void addRecordLog(String id) {
		this.recordList.addFirst(id);
		if(this.recordList.size() > 10) {
			this.recordList.removeLast();
		}
		SetChanged();
	}
	
	public void addDispatchTank(List<Integer> tankIds) {
		this.dispatchTanks.addAll(tankIds);
		SetChanged();
	}
	public void removeDispatchTank(List<Integer> tankIds) {
		this.dispatchTanks.removeAll(tankIds);
		SetChanged();
	}
	
	public void reloadDispatchTanks(Set<Integer> tankIds) {
		this.dispatchTanks = Sets.newHashSet(tankIds);
		SetChanged();
	}
	public Set<Integer> getDispatchTanks() {
		return dispatchTanks;
	}

	public NpcRobSupply getNpcRobSupply(String id) {
		return this.npcMap.get(id);
	}
	public void setNpcMap(Map<String, NpcRobSupply> npcMap) {
		this.npcMap = npcMap;
		SetChanged();
	}
	public void removeNpcRobSupply(String id) {
		this.npcMap.remove(id);
		SetChanged();
	}

	public void setFreeRefresh(boolean freeRefresh) {
		this.freeRefresh = freeRefresh;
	}
}
