package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0801_pray_target")
public class Active0801PrayTargetTemplate {
	private Integer id;
	private Integer stage;
	private Integer fireworks_num;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getFireworks_num() {
		return fireworks_num;
	}

	public void setFireworks_num(Integer fireworks_num) {
		this.fireworks_num = fireworks_num;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
