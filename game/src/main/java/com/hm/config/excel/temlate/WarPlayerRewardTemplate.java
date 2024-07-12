package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("war_player_reward")
public class WarPlayerRewardTemplate {
	private Integer index;
	private Integer war_type;
	private String task_name;
	private String task_sec;
	private Integer finish;
	private String reward;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getWar_type() {
		return war_type;
	}

	public void setWar_type(Integer war_type) {
		this.war_type = war_type;
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
	public Integer getFinish() {
		return finish;
	}

	public void setFinish(Integer finish) {
		this.finish = finish;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
