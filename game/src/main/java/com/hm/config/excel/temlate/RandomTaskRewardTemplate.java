package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("random_task_reward")
public class RandomTaskRewardTemplate {
	private Integer id;
	private Integer task_type;
	private Integer level_limit_down;
	private Integer level_limit_up;
	private String reward;

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
	public Integer getLevel_limit_down() {
		return level_limit_down;
	}

	public void setLevel_limit_down(Integer level_limit_down) {
		this.level_limit_down = level_limit_down;
	}
	public Integer getLevel_limit_up() {
		return level_limit_up;
	}

	public void setLevel_limit_up(Integer level_limit_up) {
		this.level_limit_up = level_limit_up;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
