package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("car_skill_cost")
public class CarSkillCostTemplate {
	private Integer level;
	private String cost;
	private String cost_total;
	private Integer effectiveness;

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
	public Integer getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(Integer effectiveness) {
		this.effectiveness = effectiveness;
	}
}
