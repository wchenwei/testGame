package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("capital_fight_killrank_reward")
public class CapitalFightKillrankRewardTemplate {
	private Integer id;
	private Integer kill_num;
	private Integer reward_score;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getKill_num() {
		return kill_num;
	}

	public void setKill_num(Integer kill_num) {
		this.kill_num = kill_num;
	}
	public Integer getReward_score() {
		return reward_score;
	}

	public void setReward_score(Integer reward_score) {
		this.reward_score = reward_score;
	}
}
