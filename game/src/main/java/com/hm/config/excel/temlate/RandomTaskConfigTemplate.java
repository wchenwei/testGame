package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("random_task_config")
public class RandomTaskConfigTemplate {
	private Integer id;
	private String task_name;
	private String task_title;
	private String task_dec;
	private Integer weight;
	private String target;
	private Integer intimacy_add;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getTask_title() {
		return task_title;
	}

	public void setTask_title(String task_title) {
		this.task_title = task_title;
	}
	public String getTask_dec() {
		return task_dec;
	}

	public void setTask_dec(String task_dec) {
		this.task_dec = task_dec;
	}
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	public Integer getIntimacy_add() {
		return intimacy_add;
	}

	public void setIntimacy_add(Integer intimacy_add) {
		this.intimacy_add = intimacy_add;
	}
}
