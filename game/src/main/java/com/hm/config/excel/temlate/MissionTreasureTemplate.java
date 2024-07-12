package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_treasure")
public class MissionTreasureTemplate {
	private Integer mission_id;
	private Integer level;
	private Integer unlock_level;
	private String name;
	private String icon;
	private String first_reward;
	private String reward;
	private String cost;
	private String enemy_config;
	private String enemy_pos;
	private String enemy_reward;

	public Integer getMission_id() {
		return mission_id;
	}

	public void setMission_id(Integer mission_id) {
		this.mission_id = mission_id;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getUnlock_level() {
		return unlock_level;
	}

	public void setUnlock_level(Integer unlock_level) {
		this.unlock_level = unlock_level;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getFirst_reward() {
		return first_reward;
	}

	public void setFirst_reward(String first_reward) {
		this.first_reward = first_reward;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getEnemy_config() {
		return enemy_config;
	}

	public void setEnemy_config(String enemy_config) {
		this.enemy_config = enemy_config;
	}
	public String getEnemy_pos() {
		return enemy_pos;
	}

	public void setEnemy_pos(String enemy_pos) {
		this.enemy_pos = enemy_pos;
	}
	public String getEnemy_reward() {
		return enemy_reward;
	}

	public void setEnemy_reward(String enemy_reward) {
		this.enemy_reward = enemy_reward;
	}
}
