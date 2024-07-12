package com.hm.model.player;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BeCaptiveTankInfo {
	private int tankId;//已经俘虏的坦克id
	private long playerId;
	private String name;
	private long endTime;
	
	public BeCaptiveTankInfo(Player player,int tankId, long endTime) {
		super();
		this.tankId = tankId;
		this.playerId = player.getId();
		this.name = player.getName();
		this.endTime = endTime;
	}
	
	public boolean isFitTime() {
		return this.endTime > System.currentTimeMillis();
	}

}
