package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;


public class PlayerTreasury extends PlayerDataContext {
	/**
	 * 当日金砖征收次数
	 */
	private int costTimes;
	/**
	 * 最近一次征收时间 ms
	 */
	private long lastTime;

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerTreasury", this);
	}

	//重置数据
	public void resetDay(){
		if (costTimes != 0) {
			costTimes = 0;
			SetChanged();
		}
	}

	public int getCostTimes() {
		return costTimes;
	}

	public void setCostTimes(int costTimes) {
		this.costTimes = costTimes;
		SetChanged();
	}

	public void incCostTimes() {
		this.costTimes++;
		SetChanged();
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
		SetChanged();
	}
}
