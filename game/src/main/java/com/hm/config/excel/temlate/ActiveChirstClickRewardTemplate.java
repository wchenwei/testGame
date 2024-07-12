package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_chirst_click_reward")
public class ActiveChirstClickRewardTemplate {
	private Integer id;
	private Integer server_lv_down;
	private Integer server_lv_up;
	private Integer type;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServer_lv_down() {
		return server_lv_down;
	}

	public void setServer_lv_down(Integer server_lv_down) {
		this.server_lv_down = server_lv_down;
	}
	public Integer getServer_lv_up() {
		return server_lv_up;
	}

	public void setServer_lv_up(Integer server_lv_up) {
		this.server_lv_up = server_lv_up;
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
