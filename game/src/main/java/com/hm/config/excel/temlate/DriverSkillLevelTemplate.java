package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("driver_skill_level")
public class DriverSkillLevelTemplate {
	private Integer level;
	private Integer skill_cash;
	private Integer skill_total;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getSkill_cash() {
		return skill_cash;
	}

	public void setSkill_cash(Integer skill_cash) {
		this.skill_cash = skill_cash;
	}
	public Integer getSkill_total() {
		return skill_total;
	}

	public void setSkill_total(Integer skill_total) {
		this.skill_total = skill_total;
	}
}
