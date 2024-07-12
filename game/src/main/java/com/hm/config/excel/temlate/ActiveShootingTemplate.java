package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_shooting")
public class ActiveShootingTemplate {
	private Integer index;
	private Integer circle;
	private String reward;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getCircle() {
		return circle;
	}

	public void setCircle(Integer circle) {
		this.circle = circle;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
