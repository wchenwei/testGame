package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("task_link")
public class TaskLinkTemplate {
	private Integer tasklink_id;
	private String taskid;

	public Integer getTasklink_id() {
		return tasklink_id;
	}

	public void setTasklink_id(Integer tasklink_id) {
		this.tasklink_id = tasklink_id;
	}
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
