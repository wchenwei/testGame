package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("service_merge_sign")
public class ServiceMergeSignTemplate {
	private Integer id;
	private Integer lv_down;
	private Integer lv_up;
	private Integer day;
	private String reward_sign;
	private String reward_recharge;
	private Integer recharge_gold;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
	public String getReward_sign() {
		return reward_sign;
	}

	public void setReward_sign(String reward_sign) {
		this.reward_sign = reward_sign;
	}
	public String getReward_recharge() {
		return reward_recharge;
	}

	public void setReward_recharge(String reward_recharge) {
		this.reward_recharge = reward_recharge;
	}
	public Integer getRecharge_gold() {
		return recharge_gold;
	}

	public void setRecharge_gold(Integer recharge_gold) {
		this.recharge_gold = recharge_gold;
	}
}
