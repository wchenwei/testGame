package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_treasure_circle")
public class ActiveTreasureCircleTemplate {
	private Integer id;
	private String reward;
	private Integer rate;
	private Integer circle_effect;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public Integer getCircle_effect() {
		return circle_effect;
	}

	public void setCircle_effect(Integer circle_effect) {
		this.circle_effect = circle_effect;
	}
}
