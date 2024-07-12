package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_sweeper_box")
public class MissionSweeperBoxTemplate {
	private Integer id;
	private Integer level_b;
	private Integer level_s;
	private Integer type;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevel_b() {
		return level_b;
	}

	public void setLevel_b(Integer level_b) {
		this.level_b = level_b;
	}
	public Integer getLevel_s() {
		return level_s;
	}

	public void setLevel_s(Integer level_s) {
		this.level_s = level_s;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
