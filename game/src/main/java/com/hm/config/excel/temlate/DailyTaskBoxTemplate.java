package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("daily_task_box")
public class DailyTaskBoxTemplate {
	private Integer open_rate;
	private String box_id;

	public Integer getOpen_rate() {
		return open_rate;
	}

	public void setOpen_rate(Integer open_rate) {
		this.open_rate = open_rate;
	}
	public String getBox_id() {
		return box_id;
	}

	public void setBox_id(String box_id) {
		this.box_id = box_id;
	}
}
