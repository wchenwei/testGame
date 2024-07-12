package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("agent_level_up")
public class AgentLevelUpTemplate {
	private Integer level;
	private Integer exp;
	private Integer total_exp;
	private Integer skill_cost;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public Integer getTotal_exp() {
		return total_exp;
	}

	public void setTotal_exp(Integer total_exp) {
		this.total_exp = total_exp;
	}
	public Integer getSkill_cost() {
		return skill_cost;
	}

	public void setSkill_cost(Integer skill_cost) {
		this.skill_cost = skill_cost;
	}
}
