package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_consume_tower")
public class ActiveConsumeTowerTemplate {
	private Integer id;
	private Integer lv_down;
	private Integer lv_up;
	private Integer level;
	private String reward;
	private Integer rate;
	private Integer circle_effect;
	private Integer number;
	private Integer bottom_protection;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public Integer getCircle_effect() {
		return circle_effect;
	}

	public void setCircle_effect(Integer circle_effect) {
		this.circle_effect = circle_effect;
	}
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getBottom_protection() {
		return bottom_protection;
	}

	public void setBottom_protection(Integer bottom_protection) {
		this.bottom_protection = bottom_protection;
	}
}
