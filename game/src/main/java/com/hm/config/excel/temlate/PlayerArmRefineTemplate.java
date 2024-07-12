package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("player_arm_refine")
public class PlayerArmRefineTemplate {
	private Integer level;
	private Integer star_color;
	private Integer star_num;
	private String arm_attr;
	private Float arm_attr_rate;
	private String cost_item;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getStar_color() {
		return star_color;
	}

	public void setStar_color(Integer star_color) {
		this.star_color = star_color;
	}
	public Integer getStar_num() {
		return star_num;
	}

	public void setStar_num(Integer star_num) {
		this.star_num = star_num;
	}
	public String getArm_attr() {
		return arm_attr;
	}

	public void setArm_attr(String arm_attr) {
		this.arm_attr = arm_attr;
	}
	public Float getArm_attr_rate() {
		return arm_attr_rate;
	}

	public void setArm_attr_rate(Float arm_attr_rate) {
		this.arm_attr_rate = arm_attr_rate;
	}
	public String getCost_item() {
		return cost_item;
	}

	public void setCost_item(String cost_item) {
		this.cost_item = cost_item;
	}
}
