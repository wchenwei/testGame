package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("arena_trump_reward")
public class ArenaTrumpRewardTemplate {
	private Integer id;
	private Integer victory;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVictory() {
		return victory;
	}

	public void setVictory(Integer victory) {
		this.victory = victory;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
