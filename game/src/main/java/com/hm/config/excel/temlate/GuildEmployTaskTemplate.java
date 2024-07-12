package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_employ_task")
public class GuildEmployTaskTemplate {
	private Integer id;
	private Integer task_type;
	private Integer level;
	private Integer points;
	private String task_reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTask_type() {
		return task_type;
	}

	public void setTask_type(Integer task_type) {
		this.task_type = task_type;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
	public String getTask_reward() {
		return task_reward;
	}

	public void setTask_reward(String task_reward) {
		this.task_reward = task_reward;
	}
}
