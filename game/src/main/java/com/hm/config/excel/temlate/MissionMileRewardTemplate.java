package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_mile_reward")
public class MissionMileRewardTemplate {
	private Integer id;
	private Integer chapter_id;
	private Integer mission_id;
	private String reward;
	private Integer next_mission;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getChapter_id() {
		return chapter_id;
	}

	public void setChapter_id(Integer chapter_id) {
		this.chapter_id = chapter_id;
	}
	public Integer getMission_id() {
		return mission_id;
	}

	public void setMission_id(Integer mission_id) {
		this.mission_id = mission_id;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getNext_mission() {
		return next_mission;
	}

	public void setNext_mission(Integer next_mission) {
		this.next_mission = next_mission;
	}
}
