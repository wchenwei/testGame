package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("driver_advance")
public class DriverAdvanceTemplate {
	private Integer id;
	private String name;
	private String icon;
	private Integer driver_level;
	private String cost;
	private String attri_base;
	private String attri_base_ss;
	private String attri_choose;
	private String attri_choose_ss;
	private String attri_circle;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Integer getDriver_level() {
		return driver_level;
	}

	public void setDriver_level(Integer driver_level) {
		this.driver_level = driver_level;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getAttri_base() {
		return attri_base;
	}

	public void setAttri_base(String attri_base) {
		this.attri_base = attri_base;
	}
	public String getAttri_base_ss() {
		return attri_base_ss;
	}

	public void setAttri_base_ss(String attri_base_ss) {
		this.attri_base_ss = attri_base_ss;
	}
	public String getAttri_choose() {
		return attri_choose;
	}

	public void setAttri_choose(String attri_choose) {
		this.attri_choose = attri_choose;
	}
	public String getAttri_choose_ss() {
		return attri_choose_ss;
	}

	public void setAttri_choose_ss(String attri_choose_ss) {
		this.attri_choose_ss = attri_choose_ss;
	}
	public String getAttri_circle() {
		return attri_circle;
	}

	public void setAttri_circle(String attri_circle) {
		this.attri_circle = attri_circle;
	}
}
