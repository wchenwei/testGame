package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.model.battle.BaseBattle;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerBattle extends PlayerDataContext {
	private ConcurrentHashMap<Integer, BaseBattle> map = new ConcurrentHashMap<Integer, BaseBattle>();

	//重置
	public void resetDay(){
		this.map.values().forEach(t -> {
			t.resetDay(super.Context());
		});
		SetChanged();
	}

	/**
	 * 获取玩家商店
	 * @param acitivty
	 * @return
	 */
	public BaseBattle getPlayerBattle(int id) {
		return map.get(id);
	}

	
	public void unlockBattle(BaseBattle battle) {
		this.map.put(battle.getBattleId(), battle);
		battle.resetDay(super.Context());
		SetChanged();
	}
	public boolean isUnlockBattle(int battleId) {
		return this.map.containsKey(battleId);
	}
	
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerBattle", this);
	}
}
