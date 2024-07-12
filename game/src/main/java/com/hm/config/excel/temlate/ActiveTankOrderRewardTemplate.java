package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_tank_order_reward")
public class ActiveTankOrderRewardTemplate {
	private Integer id;
	private Integer level;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer driver_num;
	private String reward_extra;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
	public Integer getDriver_num() {
		return driver_num;
	}

	public void setDriver_num(Integer driver_num) {
		this.driver_num = driver_num;
	}
	public String getReward_extra() {
		return reward_extra;
	}

	public void setReward_extra(String reward_extra) {
		this.reward_extra = reward_extra;
	}
}
