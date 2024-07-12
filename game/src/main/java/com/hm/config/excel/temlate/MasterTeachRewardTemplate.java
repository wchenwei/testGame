package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("master_teach_reward")
public class MasterTeachRewardTemplate {
	private Integer id;
	private Integer apprentice;
	private String reward;
	private Integer reward_limit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getApprentice() {
		return apprentice;
	}

	public void setApprentice(Integer apprentice) {
		this.apprentice = apprentice;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getReward_limit() {
		return reward_limit;
	}

	public void setReward_limit(Integer reward_limit) {
		this.reward_limit = reward_limit;
	}
}
