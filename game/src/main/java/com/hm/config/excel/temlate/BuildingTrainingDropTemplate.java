package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_training_drop")
public class BuildingTrainingDropTemplate {
	private Integer id;
	private Integer ground;
	private Integer type;
	private String reward;
	private String reward_buff;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGround() {
		return ground;
	}

	public void setGround(Integer ground) {
		this.ground = ground;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getReward_buff() {
		return reward_buff;
	}

	public void setReward_buff(String reward_buff) {
		this.reward_buff = reward_buff;
	}
}
