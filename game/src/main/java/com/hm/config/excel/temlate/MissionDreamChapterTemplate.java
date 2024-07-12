package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_dream_chapter")
public class MissionDreamChapterTemplate {
	private Integer id;
	private String name;
	private Integer before_mission;
	private Integer next_mission;

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
	public Integer getBefore_mission() {
		return before_mission;
	}

	public void setBefore_mission(Integer before_mission) {
		this.before_mission = before_mission;
	}
	public Integer getNext_mission() {
		return next_mission;
	}

	public void setNext_mission(Integer next_mission) {
		this.next_mission = next_mission;
	}
}
