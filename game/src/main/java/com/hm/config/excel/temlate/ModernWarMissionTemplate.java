package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("modern_war_mission")
public class ModernWarMissionTemplate {
	private Integer mission_id;
	private String reward;
	private Integer mission_map;
	private Integer start_point;
	private String enemy_config;
	private String enemy_pos;
	private Integer enemy_wave;
	private Integer boss_who;
	private Integer power_single;
	private Integer bottom_power;

	public Integer getMission_id() {
		return mission_id;
	}

	public void setMission_id(Integer mission_id) {
		this.mission_id = mission_id;
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
	public Integer getStart_point() {
		return start_point;
	}

	public void setStart_point(Integer start_point) {
		this.start_point = start_point;
	}
	public String getEnemy_config() {
		return enemy_config;
	}

	public void setEnemy_config(String enemy_config) {
		this.enemy_config = enemy_config;
	}
	public String getEnemy_pos() {
		return enemy_pos;
	}

	public void setEnemy_pos(String enemy_pos) {
		this.enemy_pos = enemy_pos;
	}
	public Integer getEnemy_wave() {
		return enemy_wave;
	}

	public void setEnemy_wave(Integer enemy_wave) {
		this.enemy_wave = enemy_wave;
	}
	public Integer getBoss_who() {
		return boss_who;
	}

	public void setBoss_who(Integer boss_who) {
		this.boss_who = boss_who;
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
}
