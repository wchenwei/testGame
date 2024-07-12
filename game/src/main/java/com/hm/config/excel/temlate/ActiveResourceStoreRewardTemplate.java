package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_resource_store_reward")
public class ActiveResourceStoreRewardTemplate {
	private Integer id;
	private Integer type;
	private Integer lv_down;
	private Integer lv_up;
	private String reward;
	private String show;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLv_down() {
		return lv_down;
	}

	public void setLv_down(Integer lv_down) {
		this.lv_down = lv_down;
	}
	public Integer getLv_up() {
		return lv_up;
	}

	public void setLv_up(Integer lv_up) {
		this.lv_up = lv_up;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}
}
