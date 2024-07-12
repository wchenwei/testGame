package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_sweeper")
public class MissionSweeperTemplate {
	private Integer id;
	private Integer type;
	private Integer level;
	private String reward;
	private String onekey_reward;
	private String map;
	private Integer power;
	private Integer next;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getOnekey_reward() {
		return onekey_reward;
	}

	public void setOnekey_reward(String onekey_reward) {
		this.onekey_reward = onekey_reward;
	}
	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}
	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}
	public Integer getNext() {
		return next;
	}

	public void setNext(Integer next) {
		this.next = next;
	}
}
