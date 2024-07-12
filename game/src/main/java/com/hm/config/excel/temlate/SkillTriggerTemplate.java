package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("skill_trigger")
public class SkillTriggerTemplate {
	private Integer id;
	private Integer type;
	private Float probability;
	private String grow;

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
	public Float getProbability() {
		return probability;
	}

	public void setProbability(Float probability) {
		this.probability = probability;
	}
	public String getGrow() {
		return grow;
	}

	public void setGrow(String grow) {
		this.grow = grow;
	}
}
