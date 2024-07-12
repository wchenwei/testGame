package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_1001_pass")
public class Active1001PassTemplate {
	private Integer id;
	private Integer stage;
	private Integer pass_level;
	private String reward_free;
	private String reward_trump;

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
	public Integer getPass_level() {
		return pass_level;
	}

	public void setPass_level(Integer pass_level) {
		this.pass_level = pass_level;
	}
	public String getReward_free() {
		return reward_free;
	}

	public void setReward_free(String reward_free) {
		this.reward_free = reward_free;
	}
	public String getReward_trump() {
		return reward_trump;
	}

	public void setReward_trump(String reward_trump) {
		this.reward_trump = reward_trump;
	}
}
