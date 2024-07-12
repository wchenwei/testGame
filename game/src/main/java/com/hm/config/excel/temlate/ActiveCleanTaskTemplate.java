package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_clean_task")
public class ActiveCleanTaskTemplate {
	private Integer task_id;
	private Integer task_type;
	private String task_name;
	private String task_sec;
	private String task_finish;
	private String task_reward;
	private Integer goto_ui;
	private Integer show;
	private Integer last_task;

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
	public String getTask_reward() {
		return task_reward;
	}

	public void setTask_reward(String task_reward) {
		this.task_reward = task_reward;
	}
	public Integer getGoto_ui() {
		return goto_ui;
	}

	public void setGoto_ui(Integer goto_ui) {
		this.goto_ui = goto_ui;
	}
	public Integer getShow() {
		return show;
	}

	public void setShow(Integer show) {
		this.show = show;
	}
	public Integer getLast_task() {
		return last_task;
	}

	public void setLast_task(Integer last_task) {
		this.last_task = last_task;
	}
}
