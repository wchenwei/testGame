package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_tank_order_mission")
public class ActiveTankOrderMissionTemplate {
	private Integer id;
	private Integer mission_map;
	private Integer enemy_wave;
	private Integer start_point;
	private String enemy_config;
	private String enemy_pos;
	private Integer power;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMission_map() {
		return mission_map;
	}

	public void setMission_map(Integer mission_map) {
		this.mission_map = mission_map;
	}
	public Integer getEnemy_wave() {
		return enemy_wave;
	}

	public void setEnemy_wave(Integer enemy_wave) {
		this.enemy_wave = enemy_wave;
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
	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}
}
