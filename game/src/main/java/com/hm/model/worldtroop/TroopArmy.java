package com.hm.model.worldtroop;

import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import com.hm.war.sg.troop.TankArmy;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TroopArmy extends TroopComponent{
	//出站的坦克列表
	private ConcurrentHashMap<Integer,TankArmy> tankMap = new ConcurrentHashMap<>();
	private int recoverId;//正在回血的坦克id
	private long lastRecoverTime;//
	@Transient
	private transient long recoverCD = GameConstants.Recover_HP_Interval;
	
	public ArrayList<TankArmy> getTankList() {
		return Lists.newArrayList(tankMap.values());
	}
	public List<TankArmy> getCloneTankList() {
		return tankMap.values().stream().map(e -> e.cloneArmy()).collect(Collectors.toList());
	}
	public ConcurrentHashMap<Integer,TankArmy> getTankMap(){
		return tankMap;
	}
	
	public void changeTankList(List<TankArmy> tankList) {
		this.tankMap = new ConcurrentHashMap<>(tankList.stream().collect(Collectors.toMap(TankArmy::getId, Function.identity())));
		SetChanged();
	}
	
	public void troopDeath() {
		this.tankMap.values().stream().forEach(e -> e.setHp(0));
		SetChanged();
	}
	
	public void changeTroopTankFullHp() {
		SetChanged();
		Player player = PlayerUtils.getPlayer(super.Context().getPlayerId());
		if(player == null) {
			getTankList().forEach(e -> e.setFullHp());
			return;
		}
		//判断俘虏
		List<Integer> beCaptiveTankIds = player.playerCaptive().getBeCaptiveTank();
		getTankList().stream().filter(e -> !beCaptiveTankIds.contains(e.getId()))
		.forEach(e -> e.setFullHp());
	}
	
	public int getRecoverId() {
		return recoverId;
	}

	public void setRecoverId(int recoverId) {
		this.recoverId = recoverId;
		SetChanged();
	}
	
	
	public long getLastRecoverTime() {
		return lastRecoverTime;
	}

	public void updateLastRecoverTime() {
		this.lastRecoverTime = System.currentTimeMillis()+GameConstants.Recover_HP_Interval*GameConstants.SECOND;
		this.recoverCD = GameConstants.Recover_HP_Interval;
		SetChanged();
	}

	public List<Integer> getTankIdList() {
		return tankMap.values().stream().map(e -> e.getId()).collect(Collectors.toList());
	}
	
	public void updateLastRecoverTime(long lastSecond) {
		this.lastRecoverTime = System.currentTimeMillis()+lastSecond*GameConstants.SECOND;
		this.recoverCD = lastSecond;
		SetChanged();
	}
	
	//获取距离上次回血,执行了多少秒
	public long getCurRecoverSecond() {
		long curSecond = this.recoverCD - (this.lastRecoverTime-System.currentTimeMillis())/GameConstants.SECOND;
		return Math.min(this.recoverCD, Math.max(0, curSecond));
	}

	public TankArmy getTankArmy(int id) {
		return this.tankMap.get(id);
	}

	public boolean isContainTankId(int tankId) {
		return tankMap.containsKey(tankId);
	}
}
