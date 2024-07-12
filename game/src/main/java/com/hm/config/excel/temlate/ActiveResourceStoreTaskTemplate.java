package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_resource_store_task")
public class ActiveResourceStoreTaskTemplate {
	private Integer task_id;
	private Integer task_type;
	private String task_icon;
	private String task_sec;
	private String task_finish;
	private String task_reward;
	private Integer goto_ui;
	private Integer finish_para;
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
	public String getTask_icon() {
		return task_icon;
	}

	public void setTask_icon(String task_icon) {
		this.task_icon = task_icon;
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
	public Integer getFinish_para() {
		return finish_para;
	}

	public void setFinish_para(Integer finish_para) {
		this.finish_para = finish_para;
	}
	public Integer getLast_task() {
		return last_task;
	}

	public void setLast_task(Integer last_task) {
		this.last_task = last_task;
	}
}
