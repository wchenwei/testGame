package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("capital_fight_killrank_combo_reward")
public class CapitalFightKillrankComboRewardTemplate {
	private Integer id;
	private Integer reward_score;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getReward_score() {
		return reward_score;
	}

	public void setReward_score(Integer reward_score) {
		this.reward_score = reward_score;
	}
}
