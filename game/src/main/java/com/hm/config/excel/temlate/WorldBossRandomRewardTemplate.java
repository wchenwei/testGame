package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("world_boss_random_reward")
public class WorldBossRandomRewardTemplate {
	private Integer id;
	private String random_reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getRandom_reward() {
		return random_reward;
	}

	public void setRandom_reward(String random_reward) {
		this.random_reward = random_reward;
	}
}
