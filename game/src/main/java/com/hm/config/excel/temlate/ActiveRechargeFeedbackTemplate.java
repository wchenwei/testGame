package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_recharge_feedback")
public class ActiveRechargeFeedbackTemplate {
	private Integer id;
	private Integer stage;
	private Integer lv_down;
	private Integer lv_up;
	private Integer stage_index;
	private String reward;
	private String circle_effect;

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
	public Integer getLv_down() {
		return lv_down;
	}

	public void setLv_down(Integer lv_down) {
		this.lv_down = lv_down;
	}
	public Integer getLv_up() {
		return lv_up;
	}

	public void setLv_up(Integer lv_up) {
		this.lv_up = lv_up;
	}
	public Integer getStage_index() {
		return stage_index;
	}

	public void setStage_index(Integer stage_index) {
		this.stage_index = stage_index;
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
