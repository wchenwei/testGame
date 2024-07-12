package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_101_pray_reward")
public class Active101PrayRewardTemplate {
	private Integer id;
	private Integer player_lv_low;
	private Integer player_lv_up;
	private String reward;
	private String random;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPlayer_lv_low() {
		return player_lv_low;
	}

	public void setPlayer_lv_low(Integer player_lv_low) {
		this.player_lv_low = player_lv_low;
	}
	public Integer getPlayer_lv_up() {
		return player_lv_up;
	}

	public void setPlayer_lv_up(Integer player_lv_up) {
		this.player_lv_up = player_lv_up;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}
}
