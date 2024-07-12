package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("daily_task_week_level")
public class DailyTaskWeekLevelTemplate {
	private Integer id;
	private Integer active_point;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getActive_point() {
		return active_point;
	}

	public void setActive_point(Integer active_point) {
		this.active_point = active_point;
	}
}
