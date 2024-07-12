package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_dream_box")
public class MissionDreamBoxTemplate {
	private Integer id;
	private Integer chapter_id;
	private Integer military_points;
	private String reward;

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
	public Integer getMilitary_points() {
		return military_points;
	}

	public void setMilitary_points(Integer military_points) {
		this.military_points = military_points;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
