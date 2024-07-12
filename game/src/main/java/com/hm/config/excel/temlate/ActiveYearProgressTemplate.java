package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_year_progress")
public class ActiveYearProgressTemplate {
	private Integer id;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer stage_index;
	private Integer paly_num;
	private String reward;
	private String circle_effect;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getStage_index() {
		return stage_index;
	}

	public void setStage_index(Integer stage_index) {
		this.stage_index = stage_index;
	}
	public Integer getPaly_num() {
		return paly_num;
	}

	public void setPaly_num(Integer paly_num) {
		this.paly_num = paly_num;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getCircle_effect() {
		return circle_effect;
	}

	public void setCircle_effect(String circle_effect) {
		this.circle_effect = circle_effect;
	}
}
