package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_level")
public class MissionLevelTemplate {
	private Integer mission_id;
	private Integer war_id;
	private Integer start_mission;
	private Integer target;
	private Integer pos_id;
	private String next_mission;
	private String mission_limit;
	private String icon;
	private String name;
	private Integer mission_map;
	private Integer enemy_wave;
	private Integer start_point;
	private String enemy_config;
	private String enemy_pos;
	private String reward;
	private Integer bottom_power;
	private Integer effect;

	public Integer getMission_id() {
		return mission_id;
	}

	public void setMission_id(Integer mission_id) {
		this.mission_id = mission_id;
	}
	public Integer getWar_id() {
		return war_id;
	}

	public void setWar_id(Integer war_id) {
		this.war_id = war_id;
	}
	public Integer getStart_mission() {
		return start_mission;
	}

	public void setStart_mission(Integer start_mission) {
		this.start_mission = start_mission;
	}
	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}
	public Integer getPos_id() {
		return pos_id;
	}

	public void setPos_id(Integer pos_id) {
		this.pos_id = pos_id;
	}
	public String getNext_mission() {
		return next_mission;
	}

	public void setNext_mission(String next_mission) {
		this.next_mission = next_mission;
	}
	public String getMission_limit() {
		return mission_limit;
	}

	public void setMission_limit(String mission_limit) {
		this.mission_limit = mission_limit;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getBottom_power() {
		return bottom_power;
	}

	public void setBottom_power(Integer bottom_power) {
		this.bottom_power = bottom_power;
	}
	public Integer getEffect() {
		return effect;
	}

	public void setEffect(Integer effect) {
		this.effect = effect;
	}
}
