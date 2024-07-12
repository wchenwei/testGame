package com.hm.action.mission.vo;

import com.google.common.collect.Lists;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.TankVo;

import java.util.List;

public class MissionResultVo {
	private List<Items> rewards = Lists.newArrayList();
	private List<TankVo> tanks = Lists.newArrayList();
	private long tankExp;
	private int playerLv;
	private long playerExp;
	private long playerAddExp;//玩家获得的经验
	private int result;//-1异常结算
	
	
	public MissionResultVo() {
		super();
	}
	public MissionResultVo(Player player) {
		this.playerLv = player.playerLevel().getLv();
		this.playerExp = player.playerLevel().getLvExp();
	}
	public MissionResultVo(List<TankVo> tanks, int playerLv,long playerExp, long playerAddExp,long tankExp) {
		super();
		this.tanks = tanks;
		this.tankExp = tankExp;
		this.playerExp = playerExp;
		this.playerLv = playerLv;
		this.playerAddExp = playerAddExp;
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public void setRewards(List<Items> rewards) {
		this.rewards = rewards;
	}
	public long getTankExp() {
		return tankExp;
	}
	public void setTankExp(long tankExp) {
		this.tankExp = tankExp;
	}
	public List<TankVo> getTanks() {
		return tanks;
	}
	public void setTanks(List<TankVo> tanks) {
		this.tanks = tanks;
	}
	public int getPlayerLv() {
		return playerLv;
	}
	public void setPlayerLv(int playerLv) {
		this.playerLv = playerLv;
	}
	public long getPlayerExp() {
		return playerExp;
	}
	public void setPlayerExp(long playerExp) {
		this.playerExp = playerExp;
	}
	public long getPlayerAddExp() {
		return playerAddExp;
	}
	public void setPlayerAddExp(long playerAddExp) {
		this.playerAddExp = playerAddExp;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	

}
