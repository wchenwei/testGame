package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_year_sign")
public class ActiveYearSignTemplate {
	private Integer id;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer day;
	private String reward_sign;
	private String reward_recharge;
	private Integer recharge_gold;
	private String reward_recharge_2;
	private Integer recharge_gold_2;
	private String reward_recharge_3;
	private Integer recharge_gold_3;

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
	public String getReward_recharge_2() {
		return reward_recharge_2;
	}

	public void setReward_recharge_2(String reward_recharge_2) {
		this.reward_recharge_2 = reward_recharge_2;
	}
	public Integer getRecharge_gold_2() {
		return recharge_gold_2;
	}

	public void setRecharge_gold_2(Integer recharge_gold_2) {
		this.recharge_gold_2 = recharge_gold_2;
	}

	public String getReward_recharge_3() {
		return reward_recharge_3;
	}

	public void setReward_recharge_3(String reward_recharge_3) {
		this.reward_recharge_3 = reward_recharge_3;
	}

	public Integer getRecharge_gold_3() {
		return recharge_gold_3;
	}

	public void setRecharge_gold_3(Integer recharge_gold_3) {
		this.recharge_gold_3 = recharge_gold_3;
	}
}
