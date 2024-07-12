package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_army_race_reward")
public class ActiveArmyRaceRewardTemplate {
	private Integer id;
	private Integer stage;
	private Integer reward_group;
	private Integer lv_down;
	private Integer lv_up;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getReward_group() {
		return reward_group;
	}

	public void setReward_group(Integer reward_group) {
		this.reward_group = reward_group;
	}
	public Integer getLv_down() {
		return lv_down;
	}

	public void setLv_down(Integer lv_down) {
		this.lv_down = lv_down;
	}
	public Integer getLv_up() {
		return lv_up;
	}

	public void setLv_up(Integer lv_up) {
		this.lv_up = lv_up;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
}
