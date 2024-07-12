package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_119_stage")
public class Active119StageTemplate {
	private Integer id;
	private String icon_resource;
	private String resource_login_title;
	private String resource_duihuan_title;
	private String cost;
	private String reward;
	private Integer reward_max;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon_resource() {
		return icon_resource;
	}

	public void setIcon_resource(String icon_resource) {
		this.icon_resource = icon_resource;
	}
	public String getResource_login_title() {
		return resource_login_title;
	}

	public void setResource_login_title(String resource_login_title) {
		this.resource_login_title = resource_login_title;
	}
	public String getResource_duihuan_title() {
		return resource_duihuan_title;
	}

	public void setResource_duihuan_title(String resource_duihuan_title) {
		this.resource_duihuan_title = resource_duihuan_title;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public Integer getReward_max() {
		return reward_max;
	}

	public void setReward_max(Integer reward_max) {
		this.reward_max = reward_max;
	}
}
