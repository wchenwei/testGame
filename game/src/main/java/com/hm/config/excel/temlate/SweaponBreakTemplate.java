package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("sweapon_break")
public class SweaponBreakTemplate {
	private Integer level;
	private Integer steps;
	private Integer lv_up_limit;
	private String cost;
	private Integer time_break;
	private String attribute_up;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}
	public Integer getLv_up_limit() {
		return lv_up_limit;
	}

	public void setLv_up_limit(Integer lv_up_limit) {
		this.lv_up_limit = lv_up_limit;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getTime_break() {
		return time_break;
	}

	public void setTime_break(Integer time_break) {
		this.time_break = time_break;
	}
	public String getAttribute_up() {
		return attribute_up;
	}

	public void setAttribute_up(String attribute_up) {
		this.attribute_up = attribute_up;
	}
}
