package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_1001_circle")
public class Active1001CircleTemplate {
	private Integer id;
	private Integer server_lv_down;
	private Integer server_lv_up;
	private String reward;
	private Integer weight;
	private Integer order;
	private Integer quality;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServer_lv_down() {
		return server_lv_down;
	}

	public void setServer_lv_down(Integer server_lv_down) {
		this.server_lv_down = server_lv_down;
	}
	public Integer getServer_lv_up() {
		return server_lv_up;
	}

	public void setServer_lv_up(Integer server_lv_up) {
		this.server_lv_up = server_lv_up;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
}
