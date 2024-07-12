package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_clean_citys")
public class ActiveCleanCitysTemplate {
	private Integer id;
	private Integer city_type;
	private String city_center;
	private String icon;
	private String reward_kill;
	private Integer city_higher;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCity_type() {
		return city_type;
	}

	public void setCity_type(Integer city_type) {
		this.city_type = city_type;
	}
	public String getCity_center() {
		return city_center;
	}

	public void setCity_center(String city_center) {
		this.city_center = city_center;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getReward_kill() {
		return reward_kill;
	}

	public void setReward_kill(String reward_kill) {
		this.reward_kill = reward_kill;
	}
	public Integer getCity_higher() {
		return city_higher;
	}

	public void setCity_higher(Integer city_higher) {
		this.city_higher = city_higher;
	}
}
