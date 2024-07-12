package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_sign")
public class ActiveSignTemplate {
	private Integer id;
	private Integer day;
	private Integer vip_double;
	private Integer round;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getVip_double() {
		return vip_double;
	}

	public void setVip_double(Integer vip_double) {
		this.vip_double = vip_double;
	}
	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
