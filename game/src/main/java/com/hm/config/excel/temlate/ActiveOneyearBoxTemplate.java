package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_oneyear_box")
public class ActiveOneyearBoxTemplate {
	private Integer id;
	private Integer lv_down;
	private Integer lv_up;
	private Integer day;
	private String reward;
	private Integer task_need;

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
	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getTask_need() {
		return task_need;
	}

	public void setTask_need(Integer task_need) {
		this.task_need = task_need;
	}
}
