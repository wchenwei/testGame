package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_single_attack")
public class MissionSingleAttackTemplate {
	private Integer id;
	private String name;
	private String reward_icon;
	private String reward_tips;
	private String event_library;
	private String box_reward;
	private Integer next_lv;
	private String reward;
	private Integer power;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getReward_icon() {
		return reward_icon;
	}

	public void setReward_icon(String reward_icon) {
		this.reward_icon = reward_icon;
	}
	public String getReward_tips() {
		return reward_tips;
	}

	public void setReward_tips(String reward_tips) {
		this.reward_tips = reward_tips;
	}
	public String getEvent_library() {
		return event_library;
	}

	public void setEvent_library(String event_library) {
		this.event_library = event_library;
	}
	public String getBox_reward() {
		return box_reward;
	}

	public void setBox_reward(String box_reward) {
		this.box_reward = box_reward;
	}
	public Integer getNext_lv() {
		return next_lv;
	}

	public void setNext_lv(Integer next_lv) {
		this.next_lv = next_lv;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}
}
