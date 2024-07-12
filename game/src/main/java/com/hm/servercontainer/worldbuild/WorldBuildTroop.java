package com.hm.servercontainer.worldbuild;

import com.hm.config.GameConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorldBuildTroop {
	private String id;
	private int campId;
	private long startTime;
	private long endTime;
	private int cityId;
	private int secondCount;
	
	public WorldBuildTroop(int campId,int cityId, String id,long conuteSecond) {
		super();
		this.campId = campId;
		this.cityId = cityId;
		this.id = id;
		this.startTime = System.currentTimeMillis();
		this.endTime = System.currentTimeMillis() + conuteSecond*GameConstants.SECOND;
	}
	
	public boolean isOver() {
		return System.currentTimeMillis() > endTime;
	}
	
	public void addTroopCount() {
		this.secondCount ++;
	}
	
	public long getTroopAdd(int secondAdd) {
		return  secondAdd * secondCount;
	}
}
