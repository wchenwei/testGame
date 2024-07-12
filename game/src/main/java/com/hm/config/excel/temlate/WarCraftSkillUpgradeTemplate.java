package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("war_craft_skill_upgrade")
public class WarCraftSkillUpgradeTemplate {
	private Integer id;
	private Integer skill_id;
	private Integer level;
	private String cost;
	private String cost_total;

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
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getCost_total() {
		return cost_total;
	}

	public void setCost_total(String cost_total) {
		this.cost_total = cost_total;
	}
}
