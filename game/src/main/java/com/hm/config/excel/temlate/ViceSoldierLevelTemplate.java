package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("vice_soldier_level")
public class ViceSoldierLevelTemplate {
	private Integer id;
	private String upgrade_cost;
	private Float attri_rate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getUpgrade_cost() {
		return upgrade_cost;
	}

	public void setUpgrade_cost(String upgrade_cost) {
		this.upgrade_cost = upgrade_cost;
	}
	public Float getAttri_rate() {
		return attri_rate;
	}

	public void setAttri_rate(Float attri_rate) {
		this.attri_rate = attri_rate;
	}
}
