package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("daily_task_config")
public class DailyTaskConfigTemplate {
	private Integer id;
	private Integer active_id;
	private Integer task_type;
	private String task_finish;
	private String task_reward;
	private Integer task_point;
	private Integer level_limit;
	private Integer finish_num;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getActive_id() {
		return active_id;
	}

	public void setActive_id(Integer active_id) {
		this.active_id = active_id;
	}
	public Integer getTask_type() {
		return task_type;
	}

	public void setTask_type(Integer task_type) {
		this.task_type = task_type;
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
	public Integer getTask_point() {
		return task_point;
	}

	public void setTask_point(Integer task_point) {
		this.task_point = task_point;
	}
	public Integer getLevel_limit() {
		return level_limit;
	}

	public void setLevel_limit(Integer level_limit) {
		this.level_limit = level_limit;
	}

	public Integer getFinish_num() {
		return finish_num;
	}

	public void setFinish_num(Integer finish_num) {
		this.finish_num = finish_num;
	}

}
