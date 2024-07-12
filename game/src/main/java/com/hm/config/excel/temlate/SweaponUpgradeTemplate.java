package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("sweapon_upgrade")
public class SweaponUpgradeTemplate {
	private Integer level;
	private String cost;
	private String attribute;
	private String percent_up;

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
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getPercent_up() {
		return percent_up;
	}

	public void setPercent_up(String percent_up) {
		this.percent_up = percent_up;
	}
}
