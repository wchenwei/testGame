package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_level_reward")
public class MissionLevelRewardTemplate {
	private Integer id;
	private Integer war_id;
	private String reward_case;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWar_id() {
		return war_id;
	}

	public void setWar_id(Integer war_id) {
		this.war_id = war_id;
	}
	public String getReward_case() {
		return reward_case;
	}

	public void setReward_case(String reward_case) {
		this.reward_case = reward_case;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
