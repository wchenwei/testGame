package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_rich_recharge")
public class ActiveRichRechargeTemplate {
	private Integer id;
	private Integer stage;
	private Integer server_lv_down;
	private Integer server_lv_up;
	private Integer stage_index;
	private Integer charge_gold;
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
	public Integer getStage_index() {
		return stage_index;
	}

	public void setStage_index(Integer stage_index) {
		this.stage_index = stage_index;
	}
	public Integer getCharge_gold() {
		return charge_gold;
	}

	public void setCharge_gold(Integer charge_gold) {
		this.charge_gold = charge_gold;
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
