package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("city_war_skill_upgrade")
public class CityWarSkillUpgradeTemplate {
	private Integer id;
	private Integer skill_id;
	private Integer skill_lv;
	private Integer unlock_city;
	private String upgrade_cost;
	private Integer upgrade_buff;
	private Integer upgrade_effect;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public Integer getSkill_lv() {
		return skill_lv;
	}

	public void setSkill_lv(Integer skill_lv) {
		this.skill_lv = skill_lv;
	}
	public Integer getUnlock_city() {
		return unlock_city;
	}

	public void setUnlock_city(Integer unlock_city) {
		this.unlock_city = unlock_city;
	}
	public String getUpgrade_cost() {
		return upgrade_cost;
	}

	public void setUpgrade_cost(String upgrade_cost) {
		this.upgrade_cost = upgrade_cost;
	}
	public Integer getUpgrade_buff() {
		return upgrade_buff;
	}

	public void setUpgrade_buff(Integer upgrade_buff) {
		this.upgrade_buff = upgrade_buff;
	}
	public Integer getUpgrade_effect() {
		return upgrade_effect;
	}

	public void setUpgrade_effect(Integer upgrade_effect) {
		this.upgrade_effect = upgrade_effect;
	}
}
