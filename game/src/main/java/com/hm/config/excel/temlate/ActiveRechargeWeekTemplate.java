package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_recharge_week")
public class ActiveRechargeWeekTemplate {
	private Integer id;
	private Integer palyer_lv_down;
	private Integer palyer_lv_up;
	private Integer recharge;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPalyer_lv_down() {
		return palyer_lv_down;
	}

	public void setPalyer_lv_down(Integer palyer_lv_down) {
		this.palyer_lv_down = palyer_lv_down;
	}
	public Integer getPalyer_lv_up() {
		return palyer_lv_up;
	}

	public void setPalyer_lv_up(Integer palyer_lv_up) {
		this.palyer_lv_up = palyer_lv_up;
	}
	public Integer getRecharge() {
		return recharge;
	}

	public void setRecharge(Integer recharge) {
		this.recharge = recharge;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
