package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("challenge_box")
public class ChallengeBoxTemplate {
	private Integer index;
	private Integer mission;
	private Integer star;
	private String reward;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getMission() {
		return mission;
	}

	public void setMission(Integer mission) {
		this.mission = mission;
	}
	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
