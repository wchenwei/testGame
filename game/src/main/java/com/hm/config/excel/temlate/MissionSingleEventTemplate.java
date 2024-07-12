package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_single_event")
public class MissionSingleEventTemplate {
	private Integer id;
	private Integer type;
	private Integer hp_change;
	private String reward;
	private Integer boss_hp;
	private Integer boss_attack;
	private Long power;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getHp_change() {
		return hp_change;
	}

	public void setHp_change(Integer hp_change) {
		this.hp_change = hp_change;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getBoss_hp() {
		return boss_hp;
	}

	public void setBoss_hp(Integer boss_hp) {
		this.boss_hp = boss_hp;
	}
	public Integer getBoss_attack() {
		return boss_attack;
	}

	public void setBoss_attack(Integer boss_attack) {
		this.boss_attack = boss_attack;
	}
	public Long getPower() {
		return power;
	}

	public void setPower(Long power) {
		this.power = power;
	}
}
