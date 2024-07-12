package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("express_task")
public class ExpressTaskTemplate {
	private Integer type;
	private Integer sub_type;
	private Integer level;
	private String reward_item;
	private Integer rate;
	private String star_info;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSub_type() {
		return sub_type;
	}

	public void setSub_type(Integer sub_type) {
		this.sub_type = sub_type;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getReward_item() {
		return reward_item;
	}

	public void setReward_item(String reward_item) {
		this.reward_item = reward_item;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public String getStar_info() {
		return star_info;
	}

	public void setStar_info(String star_info) {
		this.star_info = star_info;
	}
}
