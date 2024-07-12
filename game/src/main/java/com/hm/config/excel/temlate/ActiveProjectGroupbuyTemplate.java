package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_project_groupbuy")
public class ActiveProjectGroupbuyTemplate {
	private Integer id;
	private Integer need_point;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNeed_point() {
		return need_point;
	}

	public void setNeed_point(Integer need_point) {
		this.need_point = need_point;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
