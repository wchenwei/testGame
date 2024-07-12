package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_tank_skill_type")
public class ItemTankSkillTypeTemplate {
	private Integer skill_id;
	private String name;
	private String icon;
	private String dec;
	private Integer skill_type;
	private String change;
	private Integer skill_sub_type;
	private String parameters;
	private String last_time;
	private Integer last_times;

	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
	public Integer getSkill_type() {
		return skill_type;
	}

	public void setSkill_type(Integer skill_type) {
		this.skill_type = skill_type;
	}
	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}
	public Integer getSkill_sub_type() {
		return skill_sub_type;
	}

	public void setSkill_sub_type(Integer skill_sub_type) {
		this.skill_sub_type = skill_sub_type;
	}
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getLast_time() {
		return last_time;
	}

	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}
	public Integer getLast_times() {
		return last_times;
	}

	public void setLast_times(Integer last_times) {
		this.last_times = last_times;
	}
}
