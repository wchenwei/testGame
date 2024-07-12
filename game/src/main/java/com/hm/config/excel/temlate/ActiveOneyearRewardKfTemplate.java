package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_oneyear_reward_kf")
public class ActiveOneyearRewardKfTemplate {
	private Integer id;
	private Integer cake_num;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCake_num() {
		return cake_num;
	}

	public void setCake_num(Integer cake_num) {
		this.cake_num = cake_num;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
