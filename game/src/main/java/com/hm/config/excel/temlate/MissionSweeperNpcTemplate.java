package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_sweeper_npc")
public class MissionSweeperNpcTemplate {
	private Integer id;
	private Integer level_b;
	private Integer level_s;
	private Integer type;
	private String enemy_config;
	private String enemy_pos;
	private String reward;
	private Integer need_oil;
	private Integer power;
	private Integer power_single;
	private Integer bottom_power;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevel_b() {
		return level_b;
	}

	public void setLevel_b(Integer level_b) {
		this.level_b = level_b;
	}
	public Integer getLevel_s() {
		return level_s;
	}

	public void setLevel_s(Integer level_s) {
		this.level_s = level_s;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
	public Integer getNeed_oil() {
		return need_oil;
	}

	public void setNeed_oil(Integer need_oil) {
		this.need_oil = need_oil;
	}
	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
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
