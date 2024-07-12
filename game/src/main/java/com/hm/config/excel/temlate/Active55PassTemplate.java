package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_55_pass")
public class Active55PassTemplate {
	private Integer id;
	private Integer stage;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer pass_level;
	private String reward_free;
	private String reward_trump;
	private String reward_legend;

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
	public Integer getPlayer_lv_down() {
		return player_lv_down;
	}

	public void setPlayer_lv_down(Integer player_lv_down) {
		this.player_lv_down = player_lv_down;
	}
	public Integer getPlayer_lv_up() {
		return player_lv_up;
	}

	public void setPlayer_lv_up(Integer player_lv_up) {
		this.player_lv_up = player_lv_up;
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
	public String getReward_legend() {
		return reward_legend;
	}

	public void setReward_legend(String reward_legend) {
		this.reward_legend = reward_legend;
	}
}
