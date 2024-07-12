package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_task")
public class BuildingTaskTemplate {
	private Integer task_id;
	private Integer task_type;
	private Integer task_line;
	private String task_name;
	private String task_sec;
	private String task_finish;
	private String attri;

	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}
	public Integer getTask_type() {
		return task_type;
	}

	public void setTask_type(Integer task_type) {
		this.task_type = task_type;
	}
	public Integer getTask_line() {
		return task_line;
	}

	public void setTask_line(Integer task_line) {
		this.task_line = task_line;
	}
	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getTask_sec() {
		return task_sec;
	}

	public void setTask_sec(String task_sec) {
		this.task_sec = task_sec;
	}
	public String getTask_finish() {
		return task_finish;
	}

	public void setTask_finish(String task_finish) {
		this.task_finish = task_finish;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
}
