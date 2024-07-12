package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("duty_road_reward")
public class DutyRoadRewardTemplate {
	private Integer floor;
	private Integer floor_id;
	private String reward;

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public Integer getFloor_id() {
		return floor_id;
	}

	public void setFloor_id(Integer floor_id) {
		this.floor_id = floor_id;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
