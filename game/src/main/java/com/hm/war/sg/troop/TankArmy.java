package com.hm.war.sg.troop;

import com.google.common.collect.Maps;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.war.sg.UnitRetainType;
import org.springframework.data.annotation.Transient;

import java.util.Map;

public class TankArmy {
	/**
	 * Npc存储是npcId 由npcId找模型id
	 * 玩家坦克存储的就是 模型id -> tankId
	 */
	private int id; 
	private int index;
	private long hp = -1;//当前血量
	//保留状态
	@Transient
	private transient Map<UnitRetainType,Long> retainMap = Maps.newConcurrentMap();
	
	public TankArmy() {
		super();
	}

	public TankArmy(int index,int tankId,Player player){
		Tank tank = player.playerTank().getTank(tankId);
		this.index = index;
		this.id = tankId;
		this.hp = tank.getCurHp();
	}
	
	public TankArmy(int index,int tankId) {
		this.index = index;
		this.id = tankId;
	}
	
	public void setUnitRetainType(UnitRetainType type,long value) {
		this.retainMap.put(type, value);
		if(UnitRetainType.HP == type) {
			setHp(value);
		}
	}
	
	public boolean hasUnitRetainType(UnitRetainType type) {
		return this.retainMap.containsKey(type);
	}
	public long getUnitRetain(UnitRetainType type) {
		return this.retainMap.getOrDefault(type, 0L);
	}
	public void clearUnitRetain() {
		this.retainMap.clear();
	}

	public int getId() {
		return id;
	}
	public int getIndex() {
		return index;
	}

	public boolean isDeath() {
		return this.hp == 0;
	}
	public void setHp(long hp) {
		this.hp = Math.max(0, hp);
	}
	public void setFullHp() {
		this.hp = -1;
	}
	public boolean isFullHp() {
		return this.hp == -1;
	}
	
	public long getHp() {
		return hp;
	}
	
	public TankArmy cloneArmy(){
		TankArmy clone = new TankArmy(index, this.id);
		clone.hp = this.hp;
		return clone;
	}
}
