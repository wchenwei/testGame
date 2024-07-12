package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("daily_plan")
public class DailyPlanTemplate {
	private Integer id;
	private String desc;
	private String time;
	private String day;
	private Integer change;
	private String reward;
	private Integer display;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	public Integer getChange() {
		return change;
	}

	public void setChange(Integer change) {
		this.change = change;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}
}
