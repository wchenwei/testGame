package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("driver_skill")
public class DriverSkillTemplate {
	private Integer id;
	private Integer type;
	private String attribute;
	private Float grow;
	private Integer skill;
	private Integer skill_power;
	private String skill_name;
	private String skill_icon;
	private String skill_des;
	private String des_indexes;

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
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public Float getGrow() {
		return grow;
	}

	public void setGrow(Float grow) {
		this.grow = grow;
	}
	public Integer getSkill() {
		return skill;
	}

	public void setSkill(Integer skill) {
		this.skill = skill;
	}
	public Integer getSkill_power() {
		return skill_power;
	}

	public void setSkill_power(Integer skill_power) {
		this.skill_power = skill_power;
	}
	public String getSkill_name() {
		return skill_name;
	}

	public void setSkill_name(String skill_name) {
		this.skill_name = skill_name;
	}
	public String getSkill_icon() {
		return skill_icon;
	}

	public void setSkill_icon(String skill_icon) {
		this.skill_icon = skill_icon;
	}
	public String getSkill_des() {
		return skill_des;
	}

	public void setSkill_des(String skill_des) {
		this.skill_des = skill_des;
	}
	public String getDes_indexes() {
		return des_indexes;
	}

	public void setDes_indexes(String des_indexes) {
		this.des_indexes = des_indexes;
	}
}
