package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_smash_egg")
public class ActiveSmashEggTemplate {
	private Integer active_id;
	private String time;
	private String reward;
	private String reward_item;

	public Integer getActive_id() {
		return active_id;
	}

	public void setActive_id(Integer active_id) {
		this.active_id = active_id;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getReward_item() {
		return reward_item;
	}

	public void setReward_item(String reward_item) {
		this.reward_item = reward_item;
	}
}
