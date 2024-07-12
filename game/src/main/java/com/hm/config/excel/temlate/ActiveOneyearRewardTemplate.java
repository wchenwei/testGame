package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_oneyear_reward")
public class ActiveOneyearRewardTemplate {
	private Integer id;
	private Integer lv_down;
	private Integer lv_up;
	private Integer level;
	private String reward;
	private String reward_random;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public String getReward_random() {
		return reward_random;
	}

	public void setReward_random(String reward_random) {
		this.reward_random = reward_random;
	}
}
