package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_treasure_hunter")
public class ActiveTreasureHunterTemplate {
	private Integer wave;
	private String enemy;
	private String pos;
	private String interval;
	private Integer player_exp;
	private Integer tank_exp;
	private String reward;
	private String reward_check;
	private Integer start_wave;
	private Integer mission_map;
	private Integer bottom_power;

	public Integer getWave() {
		return wave;
	}

	public void setWave(Integer wave) {
		this.wave = wave;
	}
	public String getEnemy() {
		return enemy;
	}

	public void setEnemy(String enemy) {
		this.enemy = enemy;
	}
	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}
	public Integer getPlayer_exp() {
		return player_exp;
	}

	public void setPlayer_exp(Integer player_exp) {
		this.player_exp = player_exp;
	}
	public Integer getTank_exp() {
		return tank_exp;
	}

	public void setTank_exp(Integer tank_exp) {
		this.tank_exp = tank_exp;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getReward_check() {
		return reward_check;
	}

	public void setReward_check(String reward_check) {
		this.reward_check = reward_check;
	}
	public Integer getStart_wave() {
		return start_wave;
	}

	public void setStart_wave(Integer start_wave) {
		this.start_wave = start_wave;
	}
	public Integer getMission_map() {
		return mission_map;
	}

	public void setMission_map(Integer mission_map) {
		this.mission_map = mission_map;
	}
	public Integer getBottom_power() {
		return bottom_power;
	}

	public void setBottom_power(Integer bottom_power) {
		this.bottom_power = bottom_power;
	}
}
