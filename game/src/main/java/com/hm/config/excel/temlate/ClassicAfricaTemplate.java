package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("classic_africa")
public class ClassicAfricaTemplate {
	private Integer wave;
	private String boss_id;
	private Integer player_exp;
	private Integer tank_exp;
	private String reward;
	private Integer mission_map;
	private Integer power_single;
	private Integer bottom_power;
	private String little_tank;

	public Integer getWave() {
		return wave;
	}

	public void setWave(Integer wave) {
		this.wave = wave;
	}
	public String getBoss_id() {
		return boss_id;
	}

	public void setBoss_id(String boss_id) {
		this.boss_id = boss_id;
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
	public Integer getMission_map() {
		return mission_map;
	}

	public void setMission_map(Integer mission_map) {
		this.mission_map = mission_map;
	}
	public Integer getPower_single() {
		return power_single;
	}

	public void setPower_single(Integer power_single) {
		this.power_single = power_single;
	}
	public Integer getBottom_power() {
		return bottom_power;
	}

	public void setBottom_power(Integer bottom_power) {
		this.bottom_power = bottom_power;
	}
	public String getLittle_tank() {
		return little_tank;
	}

	public void setLittle_tank(String little_tank) {
		this.little_tank = little_tank;
	}
}
