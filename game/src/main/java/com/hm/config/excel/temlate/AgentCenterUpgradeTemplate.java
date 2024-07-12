package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("agent_center_upgrade")
public class AgentCenterUpgradeTemplate {
	private Integer level;
	private String attri;
	private Float attri_add;
	private String cost;
	private Integer agent_lv_add;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public Float getAttri_add() {
		return attri_add;
	}

	public void setAttri_add(Float attri_add) {
		this.attri_add = attri_add;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public Integer getAgent_lv_add() {
		return agent_lv_add;
	}

	public void setAgent_lv_add(Integer agent_lv_add) {
		this.agent_lv_add = agent_lv_add;
	}
}
