package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("task_daily_box")
public class TaskDailyBoxTemplate {
	private Integer id;
	private Integer open_point;
	private String items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOpen_point() {
		return open_point;
	}

	public void setOpen_point(Integer open_point) {
		this.open_point = open_point;
	}
	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}
}
