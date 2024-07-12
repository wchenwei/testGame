package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("war_craft_skill")
public class WarCraftSkillTemplate {
	private Integer book;
	private String name;
	private String desc;
	private String icon;
	private Integer skill_id;
	private Integer skill_type;
	private Integer level_max;
	private Integer skill_power;

	public Integer getBook() {
		return book;
	}

	public void setBook(Integer book) {
		this.book = book;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public Integer getSkill_type() {
		return skill_type;
	}

	public void setSkill_type(Integer skill_type) {
		this.skill_type = skill_type;
	}
	public Integer getLevel_max() {
		return level_max;
	}

	public void setLevel_max(Integer level_max) {
		this.level_max = level_max;
	}
	public Integer getSkill_power() {
		return skill_power;
	}

	public void setSkill_power(Integer skill_power) {
		this.skill_power = skill_power;
	}
}
