package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.AttributeType;
import com.hm.model.tank.Tank;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 玩家动态数据存储,此对象不保存数据库
 * @author siyunlong  
 * @date 2018年1月6日 上午9:47:45 
 * @version V1.0
 */
public class PlayerDynamicData extends PlayerDataContext{
	//玩家战力
	private long combat;
	//最强武将
	private int maxCombatTankId;
	private long maxTankCombat;
	private long troopCombat;
	//资源上限
	private ConcurrentHashMap<Integer, Double> resLimitMap = new ConcurrentHashMap<>();
	public long getCombat() {
		return combat;
	}

	public void setCombat(long combat) {
		this.combat = combat;
		SetChanged();
	}
	
	public long getTroopCombat() {
		return troopCombat;
	}

	public void changeTroopCombat(long troopCombat){
		if(troopCombat <= 0) {
			return;
		}
		this.troopCombat = troopCombat;
		SetChanged();
	}
	public long getMaxTankCombat() {
		return maxTankCombat;
	}

	public int getMaxCombatTankId() {
		return maxCombatTankId;
	}
	public boolean changeMaxCombatTank(Tank tank) {
		if(tank.getId() == this.maxCombatTankId 
				&& tank.getCombat() == this.maxTankCombat) {
			return false;
		}
		this.maxCombatTankId = tank.getId();
		this.maxTankCombat = tank.getCombat();
		SetChanged();
		return true;
	}
	
	public void calResLimit(int type,double value){
		this.resLimitMap.put(type, value);
		SetChanged();
	}
	
	public double getMaxResLimit(AttributeType type) {
		return this.resLimitMap.getOrDefault(type.getType(), 0d);
	}

	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerDynamicData", this);
	}
	
}
